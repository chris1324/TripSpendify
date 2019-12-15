package com.example.domain.Common.helloworld;

import java.math.BigDecimal;
import java.util.UUID;


public class HelloWorld {
    public static void main(String[] args) {

        BigDecimal total = new BigDecimal("2.515");
        BigDecimal a = total.divide(new BigDecimal(2),BigDecimal.ROUND_HALF_EVEN);
        BigDecimal b = total.divide(new BigDecimal(2),BigDecimal.ROUND_HALF_UP);

        System.out.println(a.add(b));

        System.out.println(a);
        System.out.println(b);


        System.out.println(UUID.randomUUID().toString());
    }

    // region Factory method -----------------------------------------------------------------------
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------

    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    // endregion Getter ----------------------------------------------------------------------------
}
