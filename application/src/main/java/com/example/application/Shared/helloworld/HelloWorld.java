package com.example.application.Shared.helloworld;

import com.example.application.Shared.baseUseCase.useCase.UseCase;

public class HelloWorld {
    public static void main(String[] args) {

    }

    // region Variables and Constructor ------------------------------------------------------------
    // endregion Variables and Constructor ---------------------------------------------------------

   // region RequestDTO  ---------------------------------------------------------------------------
    public static class RequestDTO implements UseCase.RequestDTO {
    }

    // endregion RequestDTO  -----------------------------------------------------------------------

    // region ResponseDTO  -------------------------------------------------------------------------
    public static class ResponseDTO implements UseCase.ResponseDTO{
    }

    // endregion ResponseDTO  ----------------------------------------------------------------------


    // region helper method ------------------------------------------------------------------------
    // endregion helper method ---------------------------------------------------------------------

    // region Error handing ------------------------------------------------------------------------
    public enum Err {}
    // endregion Error handing ---------------------------------------------------------------------


}
