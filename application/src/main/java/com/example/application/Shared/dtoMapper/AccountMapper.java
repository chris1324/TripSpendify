package com.example.application.Shared.dtoMapper;

import com.example.application.Shared.dto.accountDTO.AccountDTO;
import com.example.domain.Shared.queryBaseClass.account.Account;

public interface AccountMapper<Acc extends Account, AccDTO extends AccountDTO> extends DtoMapper<Acc, AccDTO> {

}
