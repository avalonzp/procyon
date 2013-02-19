/*
 * MethodReference.java
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

package com.strobel.assembler.metadata;

import com.strobel.util.ContractUtils;

import java.util.Collections;
import java.util.List;

/**
 * User: Mike Strobel
 * Date: 1/6/13
 * Time: 2:29 PM
 */
public abstract class MethodReference extends MemberReference implements IMethodSignature,
                                                                         IGenericParameterProvider {
    protected final static String CONSTRUCTOR_NAME = "<init>";
    protected final static String STATIC_INITIALIZER_NAME = "<clinit>";

    // <editor-fold defaultstate="collapsed" desc="Signature">

    public abstract TypeReference getReturnType();

    public boolean hasParameters() {
        return !getParameters().isEmpty();
    }

    public abstract List<ParameterDefinition> getParameters();
    
    public List<TypeReference> getThrownTypes() {
        return Collections.emptyList();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Method Attributes">

    @Override
    public boolean isSpecialName() {
        return CONSTRUCTOR_NAME.equals(getName()) ||
               STATIC_INITIALIZER_NAME.equals(getName());
    }

    public boolean isConstructor() {
        return MethodDefinition.CONSTRUCTOR_NAME.equals(getName());
    }

    public boolean isTypeInitializer() {
        return MethodDefinition.STATIC_INITIALIZER_NAME.equals(getName());
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Generics">

    public boolean isGenericMethod() {
        return hasGenericParameters();
    }

    @Override
    public boolean hasGenericParameters() {
        return !getGenericParameters().isEmpty();
    }

    @Override
    public boolean isGenericDefinition() {
        return hasGenericParameters() &&
               isDefinition();
    }

    public List<GenericParameter> getGenericParameters() {
        return Collections.emptyList();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Member Resolution">

    public MethodDefinition resolve() {
        final TypeReference declaringType = getDeclaringType();

        if (declaringType == null)
            throw ContractUtils.unsupported();

        return declaringType.resolve(this);
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Name and Signature Formatting">

    @Override
    public StringBuilder appendSignature(final StringBuilder sb) {
        final List<ParameterDefinition> parameters = getParameters();

        StringBuilder s = sb;
        s.append('(');

        for (int i = 0, n = parameters.size(); i < n; ++i) {
            final ParameterDefinition p = parameters.get(i);
            s = p.getParameterType().appendSignature(s);
        }

        s.append(')');
        s = getReturnType().appendSignature(s);

        return s;
    }

    @Override
    public StringBuilder appendErasedSignature(final StringBuilder sb) {
        StringBuilder s = sb;
        s.append('(');

        final List<ParameterDefinition> parameterTypes = getParameters();

        for (int i = 0, n = parameterTypes.size(); i < n; ++i) {
            s = parameterTypes.get(i).getParameterType().appendErasedSignature(s);
        }

        s.append(')');
        s = getReturnType().appendErasedSignature(s);

        return s;
    }

    // </editor-fold>
}
