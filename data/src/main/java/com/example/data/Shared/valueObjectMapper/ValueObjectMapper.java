package com.example.data.Shared.valueObjectMapper;

import com.example.domain.Shared.valueObject.contactnumber.ContactNumber;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Shared.valueObject.uri.Uri;

public interface ValueObjectMapper {

    // ---------------------------------------------------------------------------------------------

    ID mapID(String id);

    String mapID(ID id);

    // ---------------------------------------------------------------------------------------------

    Date mapDate(long date);

    long mapDate(Date date);

    // ---------------------------------------------------------------------------------------------

    MonetaryAmount mapMonetaryAmount(double amount);

    double mapMonetaryAmount(MonetaryAmount monetaryAmount);

    // ---------------------------------------------------------------------------------------------

    Amount mapAmount(double amount);

    double mapAmount(Amount amount);

    // ---------------------------------------------------------------------------------------------

    Name mapName(String name);

    String mapName(Name name);

    // ---------------------------------------------------------------------------------------------
    Uri mapUri(String uri);

    String mapUri(Uri uri);

    // ---------------------------------------------------------------------------------------------
    Note mapNote(String note);

    String mapNote(Note note);

    // ---------------------------------------------------------------------------------------------
    ContactNumber mapContactNumber(String contactNumber);

    String mapContactNumber(ContactNumber contactNumber);

    // ---------------------------------------------------------------------------------------------

    String mapEnum(Enum e);
}
