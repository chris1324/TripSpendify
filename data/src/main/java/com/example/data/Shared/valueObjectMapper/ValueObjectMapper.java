package com.example.data.Shared.valueObjectMapper;

import android.provider.ContactsContract;

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
import java.net.URI;

public class ValueObjectMapper {

    public ID mapID(String id) {
        return ID.existingId(id);
    }

    public String mapID(ID id) {
        return id.toString();
    }

    // ---------------------------------------------------------------------------------------------

    public Date mapDate(long date) {
        return Date.create(date).getValue().orElseThrow(ImpossibleState::new);
    }

    public long mapDate(Date date) {
        return date.getMillisecond();
    }
    // ---------------------------------------------------------------------------------------------

    public MonetaryAmount mapMonetaryAmount(double amount) {
        return MonetaryAmount.create(new BigDecimal(amount)).getValue().orElseThrow(ImpossibleState::new);
    }

    public double mapMonetaryAmount(MonetaryAmount monetaryAmount) {
        return monetaryAmount.getAmount().getAmount().doubleValue();
    }

    // ---------------------------------------------------------------------------------------------

    public Amount mapAmount(double amount){
        return Amount.create(new BigDecimal(amount)).getValue().orElseThrow(ImpossibleState::new);
    }

    public double mapAmont(Amount amount){
        return amount.getAmount().doubleValue();
    }

    // ---------------------------------------------------------------------------------------------

    public Name mapName(String name) {
        return Name.create(name).getValue().orElseThrow(ImpossibleState::new);
    }

    public String mapName(Name name) {
        return name.toString();
    }

    // ---------------------------------------------------------------------------------------------
    public Uri mapUri(String uri) {
        return Uri.create(uri).getValue().orElseThrow(ImpossibleState::new);
    }

    public String mapUri(Uri uri) {
        return uri.toString();
    }

    // ---------------------------------------------------------------------------------------------
    public Note mapNote(String note) {
        return Note.create(note).getValue().orElseThrow(ImpossibleState::new);
    }

    public String mapNote(Note note) {
        return note.toString();
    }

    // ---------------------------------------------------------------------------------------------
    public ContactNumber mapContactNumber(String contactNumber) {
        return ContactNumber.create(contactNumber).getValue().orElseThrow(ImpossibleState::new);
    }

    public String mapContactNumber(ContactNumber contactNumber) {
        return contactNumber.toString();
    }

}
