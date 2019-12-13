package com.example.domain.common;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatcher.*;
import static org.mockito.ArgumentCaptor.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EntityListTest {

    // region constants ------------------------------------------------------------------------------

    // endregion constants ---------------------------------------------------------------------------

    // region helper fields --------------------------------------------------------------------------

    // endregion helper fields -----------------------------------------------------------------------

    EntityList SUT;

    @Before
    public void setup() throws Exception {
        SUT = new EntityList();

    }


    // region helper methods -------------------------------------------------------------------------

    // endregion helper methods ----------------------------------------------------------------------

    // region helper classes -------------------------------------------------------------------------

    // endregion helper classes ----------------------------------------------------------------------
}