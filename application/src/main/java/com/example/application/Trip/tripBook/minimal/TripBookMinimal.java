package com.example.application.Trip.tripBook.minimal;

import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.uri.Uri;

public class TripBookMinimal {

    final public ID tripBookId;
    final public Name mTripName;
    final public Uri mPhotoUri;
    final public Date mStartDate;
    final public Date mEndDate;

    public TripBookMinimal(ID tripBookId, Name tripName, Uri photoUri, Date startDate, Date endDate) {
        this.tripBookId = tripBookId;
        mTripName = tripName;
        mPhotoUri = photoUri;
        mStartDate = startDate;
        mEndDate = endDate;
    }
}
