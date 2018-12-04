package mans.abdullah.abdullah_mansour.universitystudentssystem.PostsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mans.abdullah.abdullah_mansour.universitystudentssystem.Adapter.PostsAdapter;
import mans.abdullah.abdullah_mansour.universitystudentssystem.FullPostImage;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.Post;
import mans.abdullah.abdullah_mansour.universitystudentssystem.Models.UserData;
import mans.abdullah.abdullah_mansour.universitystudentssystem.R;
import mans.abdullah.abdullah_mansour.universitystudentssystem.ViewHolder.PostViewHolder;

public abstract class AllPostsFragment extends Fragment
{
    DatabaseReference mDatabase;
    // [END define_database_reference]

    FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private SwipeRefreshLayout swipe_refresh;

    public AllPostsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.recentposts_fragment, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        swipe_refresh = view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = view.findViewById(R.id.recent_posts_recyclerview);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        swipe_refresh.setColorSchemeResources(R.color.colorPrimary);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                pullAndRefresh();
            }
        });

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options)
        {

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
            {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.post_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(final PostViewHolder viewHolder, final int position, final Post model)
            {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // Launch PostDetailActivity
                        Toast.makeText(getContext(), "position : " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                /*if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }*/

                // Bind Post to ViewHolder, setting OnClickListener for the star button

                viewHolder.bindToPost(model, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "like clicked", Toast.LENGTH_SHORT).show();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "comment clicked", Toast.LENGTH_SHORT).show();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent = new Intent(getActivity(), FullPostImage.class);
                                intent.putExtra(FullPostImage.EXTRA_POST_KEY, postKey);
                                startActivity(intent);
                            }
                        }
                );
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    @Override
    public void onStart()
    {
        super.onStart();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    public String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void pullAndRefresh()
    {
        swipeProgress(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                firebaseRecyclerAdapter.notifyDataSetChanged();
                swipeProgress(false);
            }
        }, 3000);
    }

    private void swipeProgress(final boolean show)
    {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }

}
