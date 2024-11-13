/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.capability;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.validation.group.Create;
import org.sonatype.nexus.validation.group.CreateNonExposed;
import org.sonatype.nexus.validation.group.Delete;
import org.sonatype.nexus.validation.group.DeleteNonExposed;
import org.sonatype.nexus.validation.group.Load;
import org.sonatype.nexus.validation.group.Update;

/**
 * Describes a capability (its type). Note: Do not inject List<BlobStoreDescriptors> and instead opt to use
 * DefaultCapabilityDescriptorProvider
 */
public interface CapabilityDescriptor {
  /**
   * Returns the capability type.
   *
   * @return unique identifier of capability type
   */
  CapabilityType type();

  /**
   * Returns a user friendly name of capability (to be presented in UI).
   *
   * @return capability type name.
   */
  String name();

  /**
   * Returns capability form fields (properties).
   *
   * @return capability form fields (properties)
   */
  List<FormField> formFields();

  /**
   * Whether or not capabilities of this type are user facing = user should be able create it via UI (select it from
   * capability type drop down). Usually not exposed capabilities are building blocks for some other capabilities and
   * should not be directly be created.
   *
   * @return true if is user facing
   */
  boolean isExposed();

  /**
   * Whether or not capabilities of this type should be hidden by default. Usually hidden capabilities are managed
   * (CRUD) by some other means like for example a custom made UI.
   * <p/>
   * User will be able to see them only when turning on hidden capabilities (in UI).
   *
   * @return true if is hidden
   */
  boolean isHidden();

  /**
   * Returns a detailed description of capability type (to be presented in UI).
   *
   * @return capability type description
   */
  String about();

  /**
   * Validate properties before create/update/load.
   */
  void validate(@Nullable CapabilityIdentity id, Map<String, String> properties, ValidationMode validationMode);

  /**
   * Returns the version of descriptor. The version should change when the descriptor fields change, case when the
   * {@link #convert} method will be called upon loading of persisted capability configuration.
   *
   * @return version
   */
  int version();

  /**
   * Converts capability properties from one version to another. The method is called upon loading of capability, in
   * case that the persisted version differ from {@link #version}.
   *
   * @param properties  to be converted
   * @param fromVersion version of capability properties to be converted
   * @return converted
   */
  Map<String, String> convert(Map<String, String> properties, int fromVersion);

  enum ValidationMode {
    CREATE(Create.class),
    CREATE_NON_EXPOSED(CreateNonExposed.class),
    UPDATE(Update.class),
    LOAD(Load.class),
    DELETE(Delete.class),
    DELETE_NON_EXPOSED(DeleteNonExposed.class);

    private final Class<?> groupingClass;

    ValidationMode(final Class<?> groupingClass) {
      this.groupingClass = groupingClass;
    }

    public Class<?> getGroupingClass() {
      return groupingClass;
    }
  }

  /**
   * Returns whether the given capability properties are duplicated by another capability.
   *
   * @since 3.13
   */
  boolean isDuplicated(@Nullable CapabilityIdentity id, Map<String, String> properties);

  /**
   * Optional warning message to display when disabling the capability.
   *
   * @since 3.34
   */
  @Nullable
  default String getDisableWarningMessage() {
    return null;
  }

  /**
   * Optional warning message to display when deleting the capability.
   *
   * @since 3.34
   */
  @Nullable
  default String getDeleteWarningMessage() {
    return null;
  }
}
