/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.opentcs.guing.common.components.properties.panel;

import java.awt.event.ItemEvent;
import java.util.Comparator;
import java.util.Optional;
import javax.swing.JPanel;
import org.opentcs.data.peripherals.PeripheralOperation;
import org.opentcs.guing.base.components.properties.type.Property;
import org.opentcs.guing.base.model.PeripheralOperationModel;
import org.opentcs.guing.base.model.elements.LocationModel;
import org.opentcs.guing.common.components.dialogs.DetailsDialogContent;
import org.opentcs.guing.common.model.SystemModel;
import org.opentcs.guing.common.util.I18nPlantOverview;
import org.opentcs.thirdparty.guing.common.jhotdraw.util.ResourceBundleUtil;
import org.opentcs.util.gui.StringListCellRenderer;

/**
 * User interface for a single line text.
 *
 * @author Leonard Schüngel (Fraunhofer IML)
 */
public class PeripheralOperationPanel
    extends JPanel
    implements DetailsDialogContent {

  private static final Comparator<LocationModel> BY_NAME
      = (o1, o2) -> o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
  /**
   * The bundle to be used.
   */
  private final ResourceBundleUtil bundle
      = ResourceBundleUtil.getBundle(I18nPlantOverview.PROPERTIES_PATH);

  /**
   * Creates new form StringPanel.
   *
   * @param model The current system model.
   */
  public PeripheralOperationPanel(SystemModel model) {
    initComponents();

    model.getLocationModels().stream()
        .sorted(BY_NAME)
        .forEach(locationComboBox::addItem);
    loadOperations();

    //Load triggers
    for (PeripheralOperation.ExecutionTrigger trigger
             : PeripheralOperation.ExecutionTrigger.values()) {
      triggerComboBox.addItem(trigger);
    }
  }

  private void loadOperations() {
    LocationModel location = (LocationModel) locationComboBox.getSelectedItem();
    if (location == null) {
      return;
    }

    operationComboBox.removeAllItems();
    for (String op
             : location.getLocationType().getPropertyAllowedPeripheralOperations().getItems()) {
      operationComboBox.addItem(op);
    }
  }

  @Override
  public void updateValues() {
  }

  @Override
  public String getTitle() {
    return bundle.getString("peripheralOperationPanel.title");
  }

  @Override
  public void setProperty(Property property) {
  }

  @Override
  public Property getProperty() {
    return null;
  }

  public Optional<PeripheralOperationModel> getPeripheralOperationModel() {
    if (!isSelectionValid()) {
      return Optional.empty();
    }

    return Optional.of(new PeripheralOperationModel(
        ((LocationModel) locationComboBox.getSelectedItem()).getName(),
        (String) operationComboBox.getSelectedItem(),
        (PeripheralOperation.ExecutionTrigger) triggerComboBox.getSelectedItem(),
        requireCompleteCheckBox.isSelected()));
  }

  public void setPeripheralOpartionModel(PeripheralOperationModel model) {
    //Set location
    for (int i = 0; i < locationComboBox.getItemCount(); i++) {
      LocationModel location = locationComboBox.getItemAt(i);
      if (location.getName().equals(model.getLocationName())) {
        locationComboBox.setSelectedIndex(i);
      }
    }
    operationComboBox.setSelectedItem(model.getOperation());
    triggerComboBox.setSelectedItem(model.getExecutionTrigger());
    requireCompleteCheckBox.setSelected(model.isCompletionRequired());
  }

  private boolean isSelectionValid() {
    return locationComboBox.getSelectedItem() != null
        && operationComboBox.getSelectedItem() != null
        && triggerComboBox.getSelectedItem() != null;
  }

  // CHECKSTYLE:OFF
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    locationLabel = new javax.swing.JLabel();
    operationLabel = new javax.swing.JLabel();
    triggerLabel = new javax.swing.JLabel();
    requireCompleteLabel = new javax.swing.JLabel();
    requireCompleteCheckBox = new javax.swing.JCheckBox();
    locationComboBox = new javax.swing.JComboBox<>();
    operationComboBox = new javax.swing.JComboBox<>();
    triggerComboBox = new javax.swing.JComboBox<>();

    setLayout(new java.awt.GridBagLayout());

    locationLabel.setFont(locationLabel.getFont());
    java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/org/opentcs/plantoverview/panels/propertyEditing"); // NOI18N
    locationLabel.setText(bundle.getString("peripheralOperationPanel.label_location.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    add(locationLabel, gridBagConstraints);

    operationLabel.setFont(operationLabel.getFont());
    operationLabel.setText(bundle.getString("peripheralOperationPanel.label_operation.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    add(operationLabel, gridBagConstraints);

    triggerLabel.setFont(triggerLabel.getFont());
    triggerLabel.setText(bundle.getString("peripheralOperationPanel.label_trigger.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    add(triggerLabel, gridBagConstraints);

    requireCompleteLabel.setFont(requireCompleteLabel.getFont());
    requireCompleteLabel.setText(bundle.getString("peripheralOperationPanel.label_requireComplete.text")); // NOI18N
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
    add(requireCompleteLabel, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    add(requireCompleteCheckBox, gridBagConstraints);

    locationComboBox.setPreferredSize(new java.awt.Dimension(200, 22));
    locationComboBox.setRenderer(new StringListCellRenderer<LocationModel>(locationModel -> locationModel.getName()));
    locationComboBox.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        locationComboBoxItemStateChanged(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    add(locationComboBox, gridBagConstraints);

    operationComboBox.setPreferredSize(new java.awt.Dimension(200, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    add(operationComboBox, gridBagConstraints);

    triggerComboBox.setPreferredSize(new java.awt.Dimension(200, 22));
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
    add(triggerComboBox, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void locationComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_locationComboBoxItemStateChanged
    if (evt.getStateChange() == ItemEvent.SELECTED) {
      loadOperations();
    }
  }//GEN-LAST:event_locationComboBoxItemStateChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox<LocationModel> locationComboBox;
  private javax.swing.JLabel locationLabel;
  private javax.swing.JComboBox<String> operationComboBox;
  private javax.swing.JLabel operationLabel;
  private javax.swing.JCheckBox requireCompleteCheckBox;
  private javax.swing.JLabel requireCompleteLabel;
  private javax.swing.JComboBox<PeripheralOperation.ExecutionTrigger> triggerComboBox;
  private javax.swing.JLabel triggerLabel;
  // End of variables declaration//GEN-END:variables
  // CHECKSTYLE:ON
}
