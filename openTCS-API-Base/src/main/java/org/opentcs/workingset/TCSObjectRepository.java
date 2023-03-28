package org.opentcs.workingset;

import java.util.Set;
import java.util.function.Predicate;
import org.opentcs.data.ObjectExistsException;
import org.opentcs.data.ObjectUnknownException;
import org.opentcs.data.TCSObject;
import org.opentcs.data.TCSObjectReference;

/**
 * A container for <code>TCSObject</code>s belonging together.
 * <p>
 * Provides access to a set of data objects and ensures they have unique names.
 * </p>
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface TCSObjectRepository {

  /**
   * Adds a new object to the pool.
   *
   * @param newObject The object to be added to the pool.
   * @throws ObjectExistsException If an object with the same ID or the same name as the new one
   *                               already exists in this pool.
   */
  public void addObject(TCSObject<?> newObject) throws ObjectExistsException;

  /**
   * Uses the given object to replace an object in the pool with same name.
   *
   * @param object The replacing object.
   * @throws IllegalArgumentException If an object with the same name as the given object does not
   *                                  exist in this repository, yet, or if an object with the same
   *                                  name does exist but is an instance of a different class.
   */
  public void replaceObject(TCSObject<?> object) throws IllegalArgumentException;

  /**
   * Returns an object from the pool.
   *
   * @param ref A reference to the object to return.
   * @return The referenced object, or <code>null</code>, if no such object exists in this pool.
   */
  public TCSObject<?> getObjectOrNull(TCSObjectReference<?> ref);

  /**
   * Returns an object from the pool.
   *
   * @param ref A reference to the object to return.
   * @return The referenced object.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  public TCSObject<?> getObject(TCSObjectReference<?> ref) throws ObjectUnknownException;

  /**
   * Returns an object from the pool.
   *
   * @param <T>   The object's type.
   * @param clazz The class of the object to be returned.
   * @param ref   A reference to the object to be returned.
   * @return The referenced object, or <code>null</code>, if no such object exists in this pool or
   * if an object exists but is not an instance of the given class.
   */
  public <T extends TCSObject<T>> T getObjectOrNull(Class<T> clazz, TCSObjectReference<T> ref);

  /**
   * Returns an object from the pool.
   *
   * @param <T>   The object's type.
   * @param clazz The class of the object to be returned.
   * @param ref   A reference to the object to be returned.
   * @return The referenced object.
   * @throws ObjectUnknownException If the referenced object does not exist, or if an object exists
   *                                but is not an instance of the given class.
   */
  public <T extends TCSObject<T>> T getObject(Class<T> clazz, TCSObjectReference<T> ref) throws ObjectUnknownException;

  /**
   * Returns an object from the pool.
   *
   * @param name The name of the object to return.
   * @return The object with the given name, or <code>null</code>, if no such object exists in this
   * pool.
   */
  public TCSObject<?> getObjectOrNull(String name);

  /**
   * Returns an object from the pool.
   *
   * @param name The name of the object to return.
   * @return The object with the given name.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  public TCSObject<?> getObject(String name) throws ObjectUnknownException;

  /**
   * Returns an object from the pool.
   *
   * @param <T>   The object's type.
   * @param clazz The class of the object to be returned.
   * @param name  The name of the object to be returned.
   * @return The named object, or <code>null</code>, if no such object exists in this pool or if an
   * object exists but is not an instance of the given class.
   */
  public <T extends TCSObject<T>> T getObjectOrNull(Class<T> clazz, String name);

  /**
   * Returns an object from the pool.
   *
   * @param <T>   The object's type.
   * @param clazz The class of the object to be returned.
   * @param name  The name of the object to be returned.
   * @return The named object.
   * @throws ObjectUnknownException If no object with the given name exists in this pool or if an
   *                                object exists but is not an instance of the given class.
   */
  public <T extends TCSObject<T>> T getObject(Class<T> clazz, String name)
      throws ObjectUnknownException;

  /**
   * Returns a set of objects belonging to the given class.
   *
   * @param <T>   The objects' type.
   * @param clazz The class of the objects to be returned.
   * @return A set of objects belonging to the given class.
   */
  public <T extends TCSObject<T>> Set<T> getObjects(Class<T> clazz);

  /**
   * Returns a set of objects of the given class for which the given predicate is true.
   *
   * @param <T>       The objects' type.
   * @param clazz     The class of the objects to be returned.
   * @param predicate The predicate that must be true for returned objects.
   * @return A set of objects of the given class for which the given predicate is true. If no such
   * objects exist, the returned set is empty.
   */
  public <T extends TCSObject<T>> Set<T> getObjects(Class<T> clazz, Predicate<? super T> predicate);

  /**
   * Removes a referenced object from this pool.
   *
   * @param ref A reference to the object to be removed.
   * @return The object that was removed from the pool.
   * @throws ObjectUnknownException If the referenced object does not exist.
   */
  public TCSObject<?> removeObject(TCSObjectReference<?> ref) throws ObjectUnknownException;
}
