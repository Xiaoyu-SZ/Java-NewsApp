package com.java.tangningjing;



import com.java.tangningjing.ui.models.Friend;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Yalantis
 */
public class Utils {
    public static final List<Friend> friends = new ArrayList<>();

    static {
        friends.add(new Friend(R.drawable.anastasia, "ANASTASIA", R.color.sienna, "Sport", "Literature", "Music", "Art", "Technology"));
        friends.add(new Friend(R.drawable.anastasia, "IRENE", R.color.saffron, "Travelling", "Flights", "Books", "Painting", "Design"));
        friends.add(new Friend(R.drawable.anastasia, "KATE", R.color.green, "Sales", "Pets", "Skiing", "Hairstyles", "Сoffee"));
        friends.add(new Friend(R.drawable.anastasia, "PAUL", R.color.pink, "Android", "Development", "Design", "Wearables", "Pets"));
        friends.add(new Friend(R.drawable.anastasia, "DARIA", R.color.orange, "Design", "Fitness", "Healthcare", "UI/UX", "Chatting"));
        friends.add(new Friend(R.drawable.anastasia, "KIRILL", R.color.saffron, "Development", "Android", "Healthcare", "Sport", "Rock Music"));
        friends.add(new Friend(R.drawable.anastasia, "JULIA", R.color.green, "Cinema", "Music", "Tatoo", "Animals", "Management"));
        friends.add(new Friend(R.drawable.anastasia, "YALANTIS", R.color.purple, "Android", "IOS", "Application", "Development", "Company"));
    }
}