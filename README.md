# Hibernate Notes

### Session in Hibernate

A Session is used to get a physical connection with a database. The Session object is lightweight and designed to be 
instantiated each time an interaction is needed with the database. Persistent objects are saved and retrieved through a 
Session object.

The session objects should not be kept open for a long time because they are not usually thread safe and they should be 
created and destroyed them as needed. The main function of the Session is to offer, create, read, and delete operations 
for instances of mapped entity classes.

### Simple difference between Session and a Transaction

Let me explain what is a session and transaction with analogy.

You go to a market for shopping. This means you have created a session in market.

Now you are going to a shopkeeper to purchase item X. Here your transaction starts, you pick the item, pay the amount to
the shopkeeper, get the change back and keep it in your pocket and leave the shop with item X. Your transaction is over.

You are still in the market, so your session is still alive.

You go to another shop or same shop to purchase item Y or Z. you are starting another transaction in the same session.

Session will end once you leave the market.

Sample code example of Hibernate Session and Transaction:

```
 Session session = HibernateSessionFactoryUtil.openSession();
 session.beginTransaction();
 // do something
 session.save(myObject);
 session.getTransaction().commit();
 session.beginTransaction();
 // do something else
 List result = session.createQuery(
    "from Order o where o.invoice.number like '2%' ").list();
 session.getTransaction().commit();
 session.close();
```

### Caching in Hibernate

#### **First Level Cache**:   

A session provides all methods to access the database and is at the same time a cache holding 
all data being used in a transaction. If you load objects using a query or if you save an object or just attach it to 
the session, then the object will be added to the Persistence Context of the session.

***Persistence Context***: Objects are continuously added to the persistence context of the session until you close the 
session. If you call session.get() and the object already exists in the persistence context, then the object will not 
be retrieved from the database but from the context. Therefore, the session is called a first level cache.

Now, first level caching is something that is provided by Hibernate as default, and it's tightly coupled with a session.
So, if we try to execute a given select statement using the same session object twice, it will hit the database only once
as for the second time, it fetches the result from the Hibernates' First level cache. But but but, if we have 2 different 
session objects, and we try to trigger the same query twice, the first level cache won't save us from hitting the database
twice because each session object is having their own independent first level cache. To rescue us from this situation, 
we have the **SECOND LEVEL CACHE**.

### Second Level Cache

The second level cache in Hibernate needs extra configuration. If we are using old Hibernate, then we need to add the
cache implementation dependency( ex: JCache, ehCache, Redis) and it's integration dependency with Hibernate. Next, we 
need to configure some Hibernate properties in ***hibernate.cfg.xml*** file and also enable the entities to be Cacheable
using annotations like **@Cacheable** and **@Cache** at the Entity Class level. This is how caching would work across 
multiple session objects for the same query being triggered more than once.

Good read:
https://www.baeldung.com/hibernate-second-level-cache

### Caching in Spring boot

**Quick Read Links**

* https://www.javatpoint.com/spring-boot-caching#:~:text=It%20is%20a%20method%20level,Cacheable%20annotation%20contains%20more%20options.
* https://dzone.com/articles/spring-hibernate-ehcache-caching
* https://javadeveloperzone.com/spring-boot/spring-boot-database-cache-example/

### Lazy and Eager Loading in Hibernate

* If we have a collection of entity referenced in another entity, say we have a Student Entity and a Laptop Entity.
A student can have a list of laptops(OneToMany). So, when we try to fetch a Student from DB using Hibernate( *repo.findById(sid)* ), by default 
only the Student attributes will be fetched and any other foreign key attributes will be fetched lazily, 
  i.e. only when referenced using student.getLaptops(), a left outer join query is triggered to obtain the list of 
  laptops for that student Id. To change this default behavior and fetch the list of laptops eagerly, we can try the 
  following:
  
   ```
    @ManyToMany(fetch = FetchType.EAGER)
    List<Laptop> latops = new ArrayList<>();
   ```
  OR we can use the @Transactional to initialize a Hibernate session, which in turn will also initialize the Collection 
  attributes of the Entity classes. This annotation can be used either at Method or Class level  
  
   ```
    @Component
    class DataSetupService implements CommandLineRunner {
     @Override
     @Transaction
     public void run (String ..args) {
       // perform some database transactions
    }
   ```
  ***Unfortunately, the first solution using FetchType EAGER didn't work for me***

### States of Objects in Hibernate

* **New/Transient** : When a normal Java object is just created but not persisted/saved in the database.
* **Persisted** : When the Java object/entity is saved to database.
* **Detached** : When a session is closed, and the persisted object is modified, the modified state of the object is not
  saved. This is detached state. An object can move from detached to persisted state using update/merge methods.
* **Removed** : Deleted from database using remove()
* **Garbage Collected** : This is the last state of a Java object even without Hibernate

Hibernate handles persisting any changes to objects in the session when the session is flushed. update can fail if an 
instance of the object is already in the session. Merge should be used in that case. It merges the changes of the 
detached object with an object in the session, if it exists.

Update: if you are sure that the session does not contains an already persistent instance with the same identifier,then 
use update to save the data in hibernate.

Merge: if you want to save your modifications at any time with out knowing about the state of an session, then use 
merge() in hibernate.

When the entity instance is in the persistent state, all changes that you make to the mapped fields of this instance 
will be applied to the corresponding database records and fields upon flushing the Session. The persistent instance 
can be thought of as “online”, whereas the detached instance has gone “offline” and is not monitored for changes.

This means that when you change fields of a persistent object, you don’t have to call save, update or any of those 
methods to get these changes to the database: all you need is to commit the transaction, or flush or close the session, 
when you’re done with it. It is important to understand that all of the methods (persist, save, update, merge, 
saveOrUpdate) do not immediately result in the corresponding SQL UPDATE or INSERT statements. The actual saving of data
to the database occurs on committing the transaction or flushing the Session.

