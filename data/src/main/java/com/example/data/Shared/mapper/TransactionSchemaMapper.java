package com.example.data.Shared.mapper;

import com.example.data.Shared.transaction.TransactionSchema;
import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.commandBaseClass.transaction.Transaction;
import com.example.domain.Shared.valueObject.id.ID;

public abstract class TransactionSchemaMapper<Domain, Schema>  extends SchemaMapper<Domain, Schema>  {

    public TransactionSchemaMapper(ValueObjectMapper voMapper) {
        super(voMapper);
    }

    public TransactionSchema mapTransaction(ID tripBookId, Transaction transaction){
        return new TransactionSchema(
                this.mapID(transaction.getId()),
                this.mapID(tripBookId),
                this.mapDate(transaction.getDate()),
                this.mapMonetaryAmount(transaction.getAmount()),
                this.mapNote(transaction.getNote())
        );
    }

}
