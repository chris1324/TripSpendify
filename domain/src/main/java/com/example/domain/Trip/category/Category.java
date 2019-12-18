package com.example.domain.Trip.category;

import com.example.domain.Common.baseclass.entity.Entity;
import com.example.domain.Common.sharedvalueobject.id.ID;
import com.example.domain.Common.errorhanding.guard.Guard;
import com.example.domain.Common.errorhanding.exception.NullArgumentException;
import com.example.domain.Common.errorhanding.result.Result;
import com.example.domain.Common.sharedvalueobject.name.Name;
import com.example.domain.Common.sharedvalueobject.uri.Uri;

public class Category extends Entity {

    // region Factory method -----------------------------------------------------------------------
    public Result<Category,Err.Create> create(ID id, Name categoryName, Uri iconUri) {
        try {
            Guard.NotNull(id);
            Guard.NotNull(categoryName);
            Guard.NotNull(iconUri);
        } catch (NullArgumentException e) {
            return Result.err(Err.Create.NULL_ARGUMENT);
        }

        return Result.ok(new Category(id, categoryName, iconUri));
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
    private Name mCategoryName;
    private Uri mIconUri;

    private Category(ID id, Name categoryName, Uri iconUri) {
        super(id);
        mCategoryName = categoryName;
        mIconUri = iconUri;
    }
    // endregion Variables and Constructor ---------------------------------------------------------


    // ---------------------------------------------------------------------------------------------

    public void updateCategoryName(Name categoryName) {
        mCategoryName = categoryName;
    }

    public void updateIconUri(Uri iconUri) {
        mIconUri = iconUri;
    }

    // ---------------------------------------------------------------------------------------------

    public Name getCategoryName() {
        return mCategoryName;
    }

    public Uri getIconUri() {
        return mIconUri;
    }
}
