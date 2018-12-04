package mans.abdullah.abdullah_mansour.universitystudentssystem.PostsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import mans.abdullah.abdullah_mansour.universitystudentssystem.R;

public class MyPostsFragment extends AllPostsFragment
{
    public MyPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference)
    {
        // All my posts
        return databaseReference.child("usersposts")
                .child(getUid());
    }
}
