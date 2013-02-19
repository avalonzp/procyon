/*
 * ExceptionsAttribute.java
 *
 * Copyright (c) 2013 Mike Strobel
 *
 * This source code is subject to terms and conditions of the Apache License, Version 2.0.
 * A copy of the license can be found in the License.html file at the root of this distribution.
 * By using this source code in any fashion, you are agreeing to be bound by the terms of the
 * Apache License, Version 2.0.
 *
 * You must not remove this notice, or any other, from this software.
 */

package com.strobel.assembler.ir.attributes;

import com.strobel.assembler.metadata.TypeReference;
import com.strobel.core.ArrayUtilities;
import com.strobel.core.VerifyArgument;

import java.util.List;

/**
 * @author Mike Strobel
 */
public final class ExceptionsAttribute extends SourceAttribute {
    private final List<TypeReference> _exceptionTypes;

    public ExceptionsAttribute(final TypeReference... exceptionTypes) {
        super(
            AttributeNames.Exceptions,
            2 * (1 + VerifyArgument.noNullElements(exceptionTypes, "exceptionTypes").length)
        );
        _exceptionTypes = ArrayUtilities.asUnmodifiableList(exceptionTypes);
    }

    public List<TypeReference> getExceptionTypes() {
        return _exceptionTypes;
    }
}