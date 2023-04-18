/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package site.kicey.strategies.provider.dispatcher.dispatching;

import org.opentcs.components.Lifecycle;

/**
 * Describes a reusable dispatching (sub-)task with a life cycle.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface Phase
    extends Runnable,
            Lifecycle {

}
