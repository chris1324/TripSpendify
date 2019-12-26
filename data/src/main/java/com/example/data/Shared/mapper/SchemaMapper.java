package com.example.data.Shared.mapper;

import com.example.data.Shared.valueObjectMapper.ValueObjectMapper;
import com.example.domain.Shared.valueObject.contactnumber.ContactNumber;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.note.Note;
import com.example.domain.Shared.valueObject.numeric.Amount;
import com.example.domain.Shared.valueObject.numeric.MonetaryAmount;
import com.example.domain.Shared.valueObject.uri.Uri;

import java.util.List;
import java.util.stream.Collectors;

public abstract class SchemaMapper<Domain, Schema> implements ValueObjectMapper {

    private final ValueObjectMapper mVoMapper;

    public SchemaMapper(ValueObjectMapper voMapper) {
        mVoMapper = voMapper;
    }

    // ---------------------------------------------------------------------------------------------

    public abstract Schema mapToSchema(Domain domain);

    public abstract Domain mapToDomain(Schema schema);

    public List<Schema> mapToSchema(List<Domain> domains) {
        return domains.stream()
                .map(this::mapToSchema)
                .collect(Collectors.toList());
    }

    public List<Domain> mapToDomain(List<Schema> schemas) {
        return schemas.stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }


    // region helper method ------------------------------------------------------------------------
    @Override
    public ID mapID(String id) {
        return mVoMapper.mapID(id);
    }

    @Override
    public String mapID(ID id) {
        return mVoMapper.mapID(id);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Date mapDate(long date) {
        return mVoMapper.mapDate(date);
    }

    @Override
    public long mapDate(Date date) {
        return mVoMapper.mapDate(date);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public MonetaryAmount mapMonetaryAmount(double amount) {
        return mVoMapper.mapMonetaryAmount(amount);
    }

    @Override
    public double mapMonetaryAmount(MonetaryAmount monetaryAmount) {
        return mVoMapper.mapMonetaryAmount(monetaryAmount);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Amount mapAmount(double amount) {
        return mVoMapper.mapAmount(amount);
    }

    @Override
    public double mapAmount(Amount amount) {
        return mVoMapper.mapAmount(amount);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Name mapName(String name) {
        return mVoMapper.mapName(name);
    }

    @Override
    public String mapName(Name name) {
        return mVoMapper.mapName(name);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Uri mapUri(String uri) {
        return mVoMapper.mapUri(uri);
    }

    @Override
    public String mapUri(Uri uri) {
        return mVoMapper.mapUri(uri);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public Note mapNote(String note) {
        return mVoMapper.mapNote(note);
    }

    @Override
    public String mapNote(Note note) {
        return mVoMapper.mapNote(note);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public ContactNumber mapContactNumber(String contactNumber) {
        return mVoMapper.mapContactNumber(contactNumber);
    }

    @Override
    public String mapContactNumber(ContactNumber contactNumber) {
        return mVoMapper.mapContactNumber(contactNumber);
    }

    // ---------------------------------------------------------------------------------------------

    @Override
    public String mapEnum(Enum e) {
        return mVoMapper.mapEnum(e);
    }
    // endregion helper method ---------------------------------------------------------------------


}
