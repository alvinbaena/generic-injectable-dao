# generic-injectable-dao
JPA Generic DAOs that are injectable using the pattern describen in the page http://blog.xebia.com/2009/03/09/jpa-implementation-patterns-data-access-objects/

#Configuration
This library will not work if the project is not inside a JEE container.

To first use this library the component that has the persistence.xml file should have one EJB that extends DaoDataSource and is annotated with @Manager

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
The generic dao can be injected using CDI and a custom annotation that has the Class literal of the entity that the generic dao represents. The generic types are the Entity type and the PK Type of the entity.

For example, having an entity of type 'Entity' and PK of type 'Integer' the resulting GenericDao should be injected like so:

```java
@Inject
@GenericDao(Entity.class)
private JpaDao<Entity, Integer> entityDao;
```

The daos have the most common methods needed, CRUD, Batch CRUD, Count, NamedQueries, NativeQueries, and Pagination.

The generic dao can also be used with more than one persistence unit:

```java
@Inject
@GenericDao(Entity.class, managerName = "manager")
private JpaDao<Entity, Integer> entityDao;
```

When using the optional managerName tag the library will look for the Manager classes that have as a name the string supplied.
