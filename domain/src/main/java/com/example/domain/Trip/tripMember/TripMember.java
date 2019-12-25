package com.example.domain.Trip.tripMember;

import com.example.domain.Shared.commandBaseClass.entity.Entity;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Shared.valueObject.contactnumber.ContactNumber;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.uri.Uri;

public class TripMember extends Entity {

    // region Factory method -----------------------------------------------------------------------
    public static Result<TripMember, Err.Create> create(
            ID id,
            Name memberName,
            Uri photoUri,
            ContactNumber contactNumber) {

        try {
            Guard.NotNull(id);
            Guard.NotNull(memberName);
            Guard.NotNull(photoUri);
            Guard.NotNull(contactNumber);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new TripMember(id,memberName,photoUri,contactNumber));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err{
        public enum Create {
            NULL_ARGUMENT
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private Name mMemberName;
    private Uri mPhotoUri;
    private ContactNumber mContactNumber;

    private TripMember(ID id, Name memberName, Uri photoUri, ContactNumber contactNumber) {
        super(id);
        mMemberName = memberName;
        mPhotoUri = photoUri;
        mContactNumber = contactNumber;
    }

    // endregion Variables and Constructor ---------------------------------------------------------

    // ---------------------------------------------------------------------------------------------
    public void updateMemberName(Name memberName) {
        mMemberName = memberName;
    }

    public void updatePhotoUri(Uri photoUri) {
        mPhotoUri = photoUri;
    }

    public void updateContactNumber(ContactNumber contactNumber) {
        mContactNumber = contactNumber;
    }
    // ---------------------------------------------------------------------------------------------

    public Name getMemberName() {
        return mMemberName;
    }

    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    public ContactNumber getContactNumber() {
        return mContactNumber;
    }
}
