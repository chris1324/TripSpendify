package com.example.domain.Trip.tripbook;

import com.example.domain.Common.errorhanding.check.Check;
import com.example.domain.Common.errorhanding.guard.NullArgumentException;
import com.example.domain.Common.errorhanding.outcome.Outcome;
import com.example.domain.Common.sharedvalueobject.date.Date;
import com.example.domain.Common.entity.Entity;
import com.example.domain.Common.entity.ID;
import com.example.domain.Common.entitylist.EntityList;
import com.example.domain.Common.sharedvalueobject.name.Name;
import com.example.domain.Common.sharedvalueobject.uri.Uri;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Trip.tripexpense.TripExpense;
import com.example.domain.Trip.tripmember.TripMember;
import com.example.domain.Trip.category.Category;

import java.util.List;

public class TripBook extends Entity {

    // region Factory method -----------------------------------------------------------------------
    public static Result<TripBook, Err.Create> create(ID id,
                                                      List<Category> categories,
                                                      List<TripMember> tripMembers,
                                                      List<TripExpense> tripExpenses,
                                                      Name tripName,
                                                      Uri photoUri,
                                                      Date startDate,
                                                      Date endDate) {
        // Null checking
        try {
            Guard.NotNull(id);
            Guard.NotNull(categories);
            Guard.NotNull(tripExpenses);
            Guard.NotNull(tripName);
            Guard.NotNull(photoUri);
            Guard.NotNull(startDate);
            Guard.NotNull(endDate);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        // List empty checking
        if (Check.isEmptyString(categories)) return Result.err(Err.Create.EMPTY_CATEGORY);

        // StartDate and EndDate
        if (isInvalidDates(startDate, endDate)) return Result.err(Err.Create.START_AFTER_END_DATE);

        return Result.ok(new TripBook(
                id,
                EntityList.newList(categories),
                EntityList.newList(tripMembers),
                EntityList.newList(tripExpenses),
                tripName,
                photoUri,
                startDate,
                endDate));
    }
    // endregion Factory method---------------------------------------------------------------------

    // region Error Class --------------------------------------------------------------------------
    public static class Err {
        public enum Create {
            NULL_ARGUMENT,
            // Empty List
            EMPTY_CATEGORY,
            // Start & End date
            START_AFTER_END_DATE
        }

        public enum UpdateDate {
            INVALID_DATE
        }

        public enum RemoveCategory {
            HAD_TRANSACTION,
            LAST_CATEGORY
        }

        public enum RemoveMember {
            HAD_TRANSACTION
        }

        public enum AddExpense {
            INVALID_PAYER,
            INVALID_SPENDER,
            INVALID_CATEGORY
        }
    }
    // endregion Error Class -----------------------------------------------------------------------

    // region Variables and Constructor ------------------------------------------------------------
    private final EntityList<Category> mCategories;
    private final EntityList<TripMember> mTripMembers;
    private final EntityList<TripExpense> mTripExpenses;

    private Name mTripName;
    private Uri mPhotoUri;
    private Date mStartDate;
    private Date mEndDate;

    private TripBook(ID id,
                     EntityList<Category> categories,
                     EntityList<TripMember> tripMembers,
                     EntityList<TripExpense> tripExpenses,
                     Name tripName,
                     Uri photoUri,
                     Date startDate,
                     Date endDate) {
        super(id);
        mCategories = categories;
        mTripMembers = tripMembers;
        mTripExpenses = tripExpenses;
        mTripName = tripName;
        mPhotoUri = photoUri;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
    }
    // endregion Variables and Constructor ---------------------------------------------------------

    // ------------------------------------Photo Uri / Name ----------------------------------------
    public void updatePhoneUri(Uri photoUri) {
        mPhotoUri = photoUri;
    }

    public void updateTripName(Name tripName) {
        mTripName = tripName;
    }

    // ----------------------------------------Date-------------------------------------------------
    public Outcome<Err.UpdateDate> updateStartDate(Date newStartDate) {
        boolean isInvalidDates = isInvalidDates(newStartDate, mEndDate);
        if (isInvalidDates) return Outcome.err(Err.UpdateDate.INVALID_DATE);

        mStartDate = newStartDate;
        return Outcome.ok();
    }

    public Outcome<Err.UpdateDate> updateEndDate(Date newEndDate) {
        boolean isInvalidDates = isInvalidDates(mStartDate, newEndDate);
        if (isInvalidDates) return Outcome.err(Err.UpdateDate.INVALID_DATE);

        mEndDate = newEndDate;
        return Outcome.ok();
    }


    // ----------------------------------------Category---------------------------------------------
    public void addCategory(Category category) {
        mCategories.put(category);
    }

    public Outcome<Err.RemoveCategory> removeCategory(ID categoryId) {
        boolean hasTrans = doCategoryHasTrans(categoryId);
        if (hasTrans) return Outcome.err(Err.RemoveCategory.HAD_TRANSACTION);

        boolean isLastCategory = isLastCategory(categoryId);
        if (isLastCategory) return Outcome.err(Err.RemoveCategory.LAST_CATEGORY);

        mCategories.remove(categoryId);
        return Outcome.ok();
    }


    // ----------------------------------------TripMember-------------------------------------------
    public void addMember(TripMember tripMember) {
        mTripMembers.put(tripMember);
    }

    public Outcome<Err.RemoveMember> removeMember(ID tripMemberId) {
        boolean hasTrans = doMemberHasTrans(tripMemberId);
        if (hasTrans) return Outcome.err(Err.RemoveMember.HAD_TRANSACTION);

        mTripMembers.remove(tripMemberId);
        return Outcome.ok();
    }

    // ---------------------------------------Trip AddExpense---------------------------------------

    public Outcome<Err.AddExpense> addExpense(TripExpense tripExpense) {
        // Check if payer is valid Member
        boolean hasInvalidPayer = isInvalidMember(tripExpense.getPayers());
        if (hasInvalidPayer) return Outcome.err(Err.AddExpense.INVALID_PAYER);

        // Check if spender is valid Member
        boolean hasInvalidSpender = isInvalidMember(tripExpense.getSpenders());
        if (hasInvalidSpender) return Outcome.err(Err.AddExpense.INVALID_SPENDER);

        // Check if category is valid
        boolean isInvalidCategory = isInvalidCategory(tripExpense.getCategoryId());
        if (isInvalidCategory) return Outcome.err(Err.AddExpense.INVALID_CATEGORY);

        // Proceed
        // TODO: 14/12/2019 addExpense DomainEvent
        mTripExpenses.put(tripExpense);
        return Outcome.ok();
    }

    public void removeExpense(ID tripExpenseId) {
        mTripExpenses.remove(tripExpenseId);
    }


    // region helper method ------------------------------------------------------------------------
    // --------------------------------------Date---------------------------------------------------
    private static boolean isInvalidDates(Date startDate, Date endDate) {
        return startDate.isAfter(endDate);
    }

    // -------------------------------------Category------------------------------------------------
    private boolean doCategoryHasTrans(ID categoryId) {
        return mTripExpenses.contain(tripExpense -> tripExpense.getCategoryId() == categoryId);
    }

    private boolean isLastCategory(ID categoryId) {
        return mCategories.size() == 1 && mCategories.contain(categoryId);
    }

    private boolean isInvalidCategory(ID categoryId) {
        return !mCategories.contain(categoryId);

    }

    // --------------------------------------Member-------------------------------------------------
    private boolean doMemberHasTrans(ID tripMemberId) {
        return mTripExpenses.contain(tripExpense -> tripExpense.isThisMemberInvolved(tripMemberId));
    }

    private boolean isInvalidMember(List<ID> memberId) {
        for (ID id : memberId) {
            if (!mTripMembers.contain(id)) {
                return true;
            }
        }
        return false;
    }

    // endregion helper method ---------------------------------------------------------------------

    // region Getter -------------------------------------------------------------------------------
    public EntityList<Category> getCategories() {
        return EntityList.unmodifiableList(mCategories);
    }

    public EntityList<TripMember> getTripMembers() {
        return EntityList.unmodifiableList(mTripMembers);
    }

    public EntityList<TripExpense> getTripExpenses() {
        return EntityList.unmodifiableList(mTripExpenses);
    }

    public Name getTripName() {
        return mTripName;
    }

    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public Date getEndDate() {
        return mEndDate;
    }
    // endregion Getter ----------------------------------------------------------------------------
}
