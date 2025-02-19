/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.util.persistence.v004;

import java.util.ArrayList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@XmlTransient
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PlantModelElementTO {

  private String name = "";
  private List<PropertyTO> properties = new ArrayList<>();

  /**
   * Creates a new instance.
   */
  public PlantModelElementTO() {
  }

  @XmlAttribute(required = true)
  public String getName() {
    return name;
  }

  public PlantModelElementTO setName(@Nonnull String name) {
    requireNonNull(name, "name");
    this.name = name;
    return this;
  }

  @XmlElement(name = "property")
  public List<PropertyTO> getProperties() {
    return properties;
  }

  public PlantModelElementTO setProperties(@Nonnull List<PropertyTO> properties) {
    requireNonNull(properties, "properties");
    this.properties = properties;
    return this;
  }
}
