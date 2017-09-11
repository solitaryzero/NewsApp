package com.java.group37.newsapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import com.zhl.channeltagview.bean.ChannelItem;
import com.zhl.channeltagview.bean.GroupItem;
import com.zhl.channeltagview.listener.OnChannelItemClicklistener;
import com.zhl.channeltagview.listener.UserActionListener;
import com.zhl.channeltagview.view.ChannelTagView;

import java.util.ArrayList;

import cn.bingoogolapple.badgeview.BGABadgeTextView;

import static com.java.group37.newsapp.R.menu.drawer;

public class ChannelActivity extends AppCompatActivity {
    private ChannelTagView channelTagView;
    private ArrayList<ChannelItem> addedChannels = new ArrayList<>();
    private ArrayList<ChannelItem> unAddedChannels = new ArrayList<>();
    private ArrayList<GroupItem> unAddedItems = new ArrayList<>();
    private ACache mCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_manager);
        mCache = ACache.get(MainActivity.mactivity);
        channelTagView = (ChannelTagView) findViewById(R.id.channel_tag_view);
        initData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//最顶栏
        toolbar.setTitle("ChannelManage");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String added = "";
                if (addedChannels.size() > 0)
                    added = addedChannels.get(0).title;
                for (int i = 1; i < addedChannels.size(); i++)
                    added += " " + addedChannels.get(i).title;
                Intent intent = new Intent();
                intent.putExtra("result", added);
                //Log.e("AddedChannels", added);
                /*
                 * 调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
                 */
                setResult(2468, intent);
                finish();
            }
        });
        channelTagView.showPahtAnim(false);
//        channelTagView.oPenCategory(true);
//        channelTagView.showItemDrawableLeft(true);
//        channelTagView.setItemLeftDrawableLayoutParams(new RelativeLayout.LayoutParams(50,50));
//        channelTagView.showItemDrawableLeft(true);
//        channelTagView.setSwipeEnable(false);
//        channelTagView.setCategoryItemBg(R.color.content_color);
//        channelTagView.setCategoryItemTxColor(ContextCompat.getColor(this,R.color.content_color));
//        channelTagView.setCategoryItemTxSize(18);
//        channelTagView.setChannelItemTxColor(Color.BLUE);
//        channelTagView.setChannelItemBg(R.drawable.custom_channel_item_bg);
//        channelTagView.setCategrayUnAddedBannerTX("更多栏目");
//        channelTagView.setCategoryAddedBannerBg(R.color.content_color);
//        channelTagView.setCategoryBannerTXsize(40);
//        channelTagView.setCategoryBannerTXColor(Color.argb(255,221,224,98));
//        channelTagView.setColumnVerticalSpace(20);
        channelTagView.initChannels(addedChannels, unAddedItems, true, new ChannelTagView.RedDotRemainderListener() {

            @Override
            public boolean showAddedChannelBadge(BGABadgeTextView itemView, int position) {
                if (addedChannels.get(position).title.equals("直播")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean showUnAddedChannelBadge(BGABadgeTextView itemView, int position) {
                if (unAddedChannels.get(position).title.equals("数码") || unAddedChannels.get(position).title.equals("科技")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void handleAddedChannelReddot(BGABadgeTextView itemView, int position) {
                itemView.showCirclePointBadge();
            }

            @Override
            public void handleUnAddedChannelReddot(BGABadgeTextView itemView, int position) {
                if (unAddedChannels.get(position).title.equals("科技")) {
                    itemView.showTextBadge("new");
                } else {
                    itemView.showCirclePointBadge();
                }
            }

            @Override
            public void OnDragDismiss(BGABadgeTextView itemView, int position) {
                Toast.makeText(ChannelActivity.this, "拖拽取消红点提示-", Toast.LENGTH_SHORT).show();
                itemView.hiddenBadge();
            }

        });
//        channelTagView.setFixedChannelBg(R.drawable.fixed_item_bg);
//        channelTagView.setFixedChannel(0);
//        channelTagView.showPahtAnim(true);
        channelTagView.setOnChannelItemClicklistener(new OnChannelItemClicklistener() {

            @Override
            public void onAddedChannelItemClick(View itemView, int position) {
                Toast.makeText(ChannelActivity.this, "打开-" + addedChannels.get(position).title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUnAddedChannelItemClick(View itemView, int position) {
                ChannelItem item = unAddedChannels.remove(position);
                addedChannels.add(item);
                Toast.makeText(ChannelActivity.this, "添加频道-" + item.title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDrawableClickListener(View itemView, int position) {
                Toast.makeText(ChannelActivity.this, "删除-" + ChannelActivity.this.addedChannels.get(position).title, Toast.LENGTH_SHORT).show();
                unAddedChannels.add(addedChannels.remove(position));
            }
        });
        channelTagView.setUserActionListener(new UserActionListener() {
            @Override
            public void onMoved(int fromPos, int toPos, ArrayList<ChannelItem> checkedChannels) {
                Toast.makeText(ChannelActivity.this, "将-" + addedChannels.get(fromPos).title + " 换到 " + addedChannels.get(toPos).title, Toast.LENGTH_SHORT).show();
                addedChannels.clear();
                addedChannels.addAll(checkedChannels);
            }

            @Override
            public void onSwiped(int position, View itemView, ArrayList<ChannelItem> checkedChannels, ArrayList<ChannelItem> uncheckedChannels) {
                Toast.makeText(ChannelActivity.this, "删除-" + ChannelActivity.this.addedChannels.remove(position).title, Toast.LENGTH_SHORT).show();
                unAddedChannels.clear();
                unAddedChannels.addAll(uncheckedChannels);
            }
        });
    }

    private void initData() {
        //String[] chanles = getResources().getStringArray(R.array.chanles);
        String tmpTitles = mCache.getAsString("TitlesSavedInCache");
        String[] chanles = new String[]{"全部","科技","教育","军事","国内","社会","文化","汽车","国际","体育","财经","健康","娱乐"};
        if (tmpTitles == null)
        {
            for (int i = 0; i < chanles.length; i++)
            {
                ChannelItem item = new ChannelItem();
                item.id = i;
                item.title = chanles[i];
                item.category = "";
                addedChannels.add(item);
            }
            tmpTitles = chanles[0];
            for (int i = 1; i < chanles.length; i++)
                tmpTitles += " " + chanles[i];
            mCache.put("TitlesSavedInCache",tmpTitles);

            GroupItem groupFinance = new GroupItem();
            groupFinance.category = "其它";
            unAddedItems.add(groupFinance);
        }
        else
        {
            String listTitles[] = tmpTitles.split(" ");
            for (int i = 0; i < listTitles.length; i++)
            {
                ChannelItem item = new ChannelItem();
                item.id = i;
                item.title = listTitles[i];
                item.category = "";
                addedChannels.add(item);
            }
            GroupItem groupFinance = new GroupItem();
            groupFinance.category = "其它";

            for (int i = 0; i < chanles.length; i++)
            {
                int flag = 0;
                for (int j = 0; j < listTitles.length; j++)
                    if (chanles[i].equals(listTitles[j]))
                    {
                        flag = 1;
                        break;
                    }
                if (flag == 0)
                {
                    ChannelItem item = new ChannelItem();
                    item.id = i;
                    item.title = chanles[i];
                    item.category = "其它";
                    unAddedChannels.add(item);
                    groupFinance.addChanelItem(item);
                }
            }
            unAddedItems.add(groupFinance);
        }

    }
}

