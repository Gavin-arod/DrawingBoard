package com.board.draw.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.board.draw.R;
import com.board.draw.adapter.BrushSelectAdapter;
import com.board.draw.constants.BrushType;
import com.board.draw.dialog.base.BaseDialogView;
import com.board.draw.impl.OnBrushTypeSelectListener;
import com.board.draw.util.AssetsUtil;
import com.board.draw.util.SPUtil;
import com.board.draw.util.ScreenUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

/**
 * brush selection dialog
 */
public class SelectBrushDialog extends BaseDialogView implements OnBrushTypeSelectListener {

    public SelectBrushDialog(Context context) {
        super(context, (int) (ScreenUtil.getWidth(context) / 3f), (int) (ScreenUtil.getWidth(context) * 2f / 3f / 3f));
        setPopupGravity(Gravity.CENTER);
        setBackgroundColor(R.color.transparent);
        setOutSideDismiss(true);
        setOutSideTouchable(false);
        setContentView(R.layout.dialog_select_brush);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        AppCompatTextView tvBrushType = contentView.findViewById(R.id.dialog_title);
        tvBrushType.setTypeface(AssetsUtil.getAssetsFont(getContext()));

        RecyclerView rvBrush = contentView.findViewById(R.id.rv_brush_list);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rvBrush.setLayoutManager(manager);
        rvBrush.requestFocusFromTouch();
        BrushSelectAdapter adapter = new BrushSelectAdapter(getContext(), this);
        rvBrush.setAdapter(adapter);

        List<BrushType> data = initData();
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    private List<BrushType> initData() {
        List<BrushType> brushTypeList = new ArrayList<>();
        BrushType brushType = new BrushType();
        brushType.setType(200);
        brushType.setName("??????");
        brushTypeList.add(brushType);

        BrushType brushType2 = new BrushType();
        brushType2.setType(201);
        brushType2.setName("?????????");
        brushTypeList.add(brushType2);

        BrushType brushType3 = new BrushType();
        brushType3.setType(202);
        brushType3.setName("?????????1");
        brushTypeList.add(brushType3);

        BrushType brushType4 = new BrushType();
        brushType4.setType(203);
        brushType4.setName("?????????2");
        brushTypeList.add(brushType4);

        BrushType brushType5 = new BrushType();
        brushType5.setType(204);
        brushType5.setName("?????????3");
        brushTypeList.add(brushType5);

        BrushType brushType6 = new BrushType();
        brushType6.setType(205);
        brushType6.setName("?????????4");
        brushTypeList.add(brushType6);

        BrushType brushType9 = new BrushType();
        brushType9.setType(208);
        brushType9.setName("?????????5");
        brushTypeList.add(brushType9);

        BrushType brushType7 = new BrushType();
        brushType7.setType(206);
        brushType7.setName("?????????");
        brushTypeList.add(brushType7);

        BrushType brushType10 = new BrushType();
        brushType10.setType(209);
        brushType10.setName("?????????");
        brushTypeList.add(brushType10);

//        BrushType brushType8 = new BrushType();
//        brushType8.setType(207);
//        brushType8.setName("?????????");
//        brushTypeList.add(brushType8);

        return brushTypeList;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toShow();
    }


    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER)
                .toDismiss();
    }

    @Override
    public void onSelectBrushType(BrushType type) {
        //post select brush type notification
        EventBus.getDefault().post(type);
        SPUtil.putInt("selectedBrushType", type.getType());
        dismiss();
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        getPopupWindow().setFocusable(false);
    }
}
