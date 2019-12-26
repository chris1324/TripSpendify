package com.example.data.Shared.valueObjectMapper;

import com.example.domain.Shared.errorhanding.exception.ImpossibleState;
import com.example.domain.Shared.valueObject.contactnumber.ContactNumber;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Shared.valueObject.uri.Uri;

import java.math.BigDecimal;

public class ValueObjectMapperImpl implements ValueObjectMapper {

    @Override
    public ID mapID(String id) {
        return ID.existingId(id);
    }

    @Override
    public String mapID(ID id) {
        return id.toString();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Date mapDate(long date) {
        return Date.create(date).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public long mapDate(Date date) {
        return date.getMillisecond();
    }
    // ---------------------------------------------------------------------------------------------

    @Override
    public MonetaryAmount mapMonetaryAmount(double amount) {
        return MonetaryAmount.create(new BigDecimal(amount)).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public double mapMonetaryAmount(MonetaryAmount monetaryAmount) {
        return monetaryAmount.getAmount().getAmount().doubleValue();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Amount mapAmount(double amount) {
        return Amount.create(new BigDecimal(amount)).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public double mapAmount(Amount amount) {
        return amount.getAmount().doubleValue();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Name mapName(String name) {
        return Name.create(name).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public String mapName(Name name) {
        return name.toString();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Uri mapUri(String uri) {
        return Uri.create(uri).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public String mapUri(Uri uri) {
        return uri.toString();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public Note mapNote(String note) {
        return Note.create(note).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public String mapNote(Note note) {
        return note.toString();
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    public ContactNumber mapContactNumber(String contactNumber) {
        return ContactNumber.create(contactNumber).getValue().orElseThrow(ImpossibleState::new);
    }

    @Override
    public String mapContactNumber(ContactNumber contactNumber) {
        return contactNumber.toString();
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public String mapEnum(Enum e) {
        return e.toString();
    }
}
