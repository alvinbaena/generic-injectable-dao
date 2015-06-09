# generic-injectable-dao
JPA Generic DAOs that are injectable using the pattern described in the page http://blog.xebia.com/2009/03/09/jpa-implementation-patterns-data-access-objects/

#Configuration
This library will not work if the project is not inside a JEE container (JBoss, Glassfish, TomEE, etc).

To first use this library the component (EJB, WAR) that has the persistence.xml file should have one stateless session bean that extends DaoDataSource and is annotated with @Manager

```java
@Stateless
@Manager(lookup = "java:global/jndi", defaultManager = true, name = "manager")
public class GenericDataSource implements DaoDataSource {

    @PersistenceContext
    private EntityManager genericEm;

    @Override
    public EntityManager getEntityManager() {
        return genericEm;
    }
}
```

#Usage
The generic dao can be injected using CDI and a custom annotation that has the Class literal of the entity that the generic dao represents. The generic types are the Entity type and the Type of the primary key (PK) of the entity.

For example, having an entity of type 'Entity' and PK of type 'Integer' the resulting GenericDao should be injected like so:

```java
@Inject
@GenericDao(Entity.class)
private BaseJpaDao<Entity, Integer> entityDao;
```

There is a ParameterBuilder helper class that can be used instead of a Map<String, Object> (Probably useless, but whatever).

```java
ParameterBuilder builder = new ParameterBuilder()
                    .setParameter("param1", param1)
                    .setParameter("param2", param2);
            
List<Entity> list = entityDao.findWithNamedQuery("Entity.namedQuery", builder.build());
```

The DAOs have the most common methods needed, CRUD, Batch CRUD, Count, NamedQueries, NativeQueries, and Pagination (Currently Untested).

The generic DAO can also be used with more than one persistence unit:

```java
@Inject
@GenericDao(Entity.class, managerName = "manager")
private BaseJpaDao<Entity, Integer> entityDao;
```

When using the optional managerName tag the library will look for the @Manager classes that have a name (attribute of Manager annotation) that is the same as the string supplied.
