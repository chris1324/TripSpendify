package com.example.domain.Trip.tripBook;

import com.example.domain.Trip.tripEvent.TripBookSaved;
import com.example.domain.Shared.commandBaseClass.book.Book;
import com.example.domain.Shared.errorhanding.check.Check;
import com.example.domain.Shared.errorhanding.exception.NullArgumentException;
import com.example.domain.Shared.errorhanding.exception.UnexpectedEnumValue;
import com.example.domain.Shared.errorhanding.outcome.Outcome;
import com.example.domain.Shared.valueObject.date.Date;
import com.example.domain.Shared.valueObject.id.ID;
import com.example.domain.Shared.entityList.EntityList;
import com.example.domain.Shared.valueObject.name.Name;
import com.example.domain.Shared.valueObject.uri.Uri;
import com.example.domain.Shared.errorhanding.guard.Guard;
import com.example.domain.Shared.errorhanding.result.Result;
import com.example.domain.Trip.tripEvent.TripExpenseSaved;
import com.example.domain.Trip.tripExpense.TripExpense;
import com.example.domain.Trip.tripMember.TripMember;
import com.example.domain.Trip.category.Category;

import java.util.List;

public class TripBook extends Book  {

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
        if (Check.isEmptyList(categories)) return Result.err(Err.Create.EMPTY_CATEGORY);

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
            EMPTY_CATEGORY,
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

        public enum SaveExpense {
            PAYER_INVALID,
            SPENDER_INVALID,
            CATEGORY_INVALID,
            USER_NOT_INVOLVED
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

        super.addDomainEvent(new TripBookSaved(this.getId()));
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

    public boolean isDuringTrip(Date date) {
        boolean isBeforeStartDate = mStartDate.isBefore(date);
        boolean isAfterEndDate = mEndDate.isAfter(date);
        return isBeforeStartDate && isAfterEndDate;
    }

    // ----------------------------------------Category---------------------------------------------
    public void saveCategory(Category category) {
        mCategories.put(category);
    }

    private Outcome<Err.RemoveCategory> removeCategory(ID categoryId) {
        // !! Currently not supported !!
        boolean hasTrans = this.doCategoryHasTrans(categoryId);
        if (hasTrans) return Outcome.err(Err.RemoveCategory.HAD_TRANSACTION);

        boolean isLastCategory = this.isLastCategory(categoryId);
        if (isLastCategory) return Outcome.err(Err.RemoveCategory.LAST_CATEGORY);

        // Success
        mCategories.remove(categoryId);
        return Outcome.ok();
    }


    // ----------------------------------------TripMember-------------------------------------------
    public void saveMember(TripMember tripMember) {
        mTripMembers.put(tripMember);
    }

    public Outcome<Err.RemoveMember> removeMember(ID tripMemberId) {
        boolean hasTrans = doMemberHasTrans(tripMemberId);
        if (hasTrans) return Outcome.err(Err.RemoveMember.HAD_TRANSACTION);

        // Success
        mTripMembers.remove(tripMemberId);
        return Outcome.ok();
    }

    // ---------------------------------------Trip SaveExpense---------------------------------------
    public Outcome<Err.SaveExpense> saveExpense(TripExpense tripExpense) {
        // Check if payer is valid Member
        boolean hasInvalidPayer = isInvalidMember(tripExpense.getPayers());
        if (hasInvalidPayer) return Outcome.err(Err.SaveExpense.PAYER_INVALID);

        // Check if spender is valid Member
        boolean hasInvalidSpender = isInvalidMember(tripExpense.getSpenders());
        if (hasInvalidSpender) return Outcome.err(Err.SaveExpense.SPENDER_INVALID);

        // Check if category is valid
        boolean isInvalidCategory = isInvalidCategory(tripExpense.getCategoryId());
        if (isInvalidCategory) return Outcome.err(Err.SaveExpense.CATEGORY_INVALID);

        // Check if User is involved
        boolean userNotInvolved = userNotInvolved(tripExpense);
        if (userNotInvolved) return Outcome.err(Err.SaveExpense.USER_NOT_INVOLVED);

        // Proceed
        mTripExpenses.put(tripExpense);
        super.addDomainEvent(new TripExpenseSaved(this.getId(), tripExpense.getId()));
        return Outcome.ok();
    }

    private boolean userNotInvolved(TripExpense tripExpense) {
        boolean userIsPaying;
        boolean userIsSpending;

        switch (tripExpense.getPaymentDetail().whoIsPaying().getAnswer()) {
            case USER:
            case USER_AND_MEMBER:
            case UNPAID:
                userIsPaying = true;
                break;
            case MEMBER:
                userIsPaying = false;
                break;
            default:
                throw new UnexpectedEnumValue();
        }
        switch (tripExpense.getSplittingDetail().whoIsSpending().getAnswer()) {
            case USER:
            case USER_AND_MEMBER:
                userIsSpending = true;
                break;
            case MEMBER:
                userIsSpending = false;
                break;
            default:
                throw new UnexpectedEnumValue();
        }

        return !(userIsPaying) && !(userIsSpending);

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
        return mCategories.unmodifiable();
    }

    public EntityList<TripMember> getTripMembers() {
        return mTripMembers.unmodifiable();
    }

    public EntityList<TripExpense> getTripExpenses() {
        return mTripExpenses.unmodifiable();
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

    public int getTripNumberOfDay() {
        return getStartDate().differentInDay(getEndDate());
    }
    // endregion Getter ----------------------------------------------------------------------------
}
