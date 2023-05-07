package org.opentcs.kernelbase.peripherals;

import javax.annotation.Nonnull;
import org.opentcs.components.Lifecycle;
import org.opentcs.data.model.Location;
import org.opentcs.data.model.TCSResourceReference;
import org.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;

public interface PeripheralAttachmentManager extends Lifecycle {
  /**
   * Attaches a peripheral comm adapter to a location.
   *
   * @param location The location to attach to.
   * @param description The description of the comm adapter to attach.
   */
  void attachAdapterToLocation(@Nonnull TCSResourceReference<Location> location,
      @Nonnull PeripheralCommAdapterDescription description);

  /**
   * Returns the attachment information for a location.
   *
   * @param location The location to get attachment information about.
   * @return The attachment information for a location.
   */
  @Nonnull
  PeripheralAttachmentInformation getAttachmentInformation(
      @Nonnull TCSResourceReference<Location> location);
}
