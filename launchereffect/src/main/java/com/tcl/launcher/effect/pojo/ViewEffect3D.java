package com.tcl.launcher.effect.pojo;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.View;

import com.tcl.launcher.effect.page.ClipView;
import com.tcl.launcher.effect.page.ICompatibleView;
import com.tcl.launcher.effect.page.PageTransformationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public class ViewEffect3D {

    private PageTransformationInfo mPageTransformationInfo;
    private ICompatibleView mView;

    private boolean isValid = false;
    private boolean isPage = false;

    /**
     * 位置坐标
     */
    private float x = 0f;
    private float y = 0f;
    // private float z = 0f;

    /**
     * 旋转角度
     */
    private float mRotationDegreeX = 0f;
    private float mRotationDegreeY = 0f;
    private float mRotationDegreeZ = 0f;

    PointF mTranslation = new PointF();

    /**
     * 旋转点坐标
     */
    public float mRotationX = 0f;
    public float mRotationY = 0f;
    public float mRotationZ = 0f;

    /**
     * 缩放
     */
    public float mScaleX = 1.0f;
    public float mScaleY = 1.0f;
    public float mScaleZ = 1.0f;

    /**
     * camera default distance is -8 in android view system!
     */
    private float mCameraDistance = -8;

    /**
     * 子View 切割块列表
     */
    List<ClipView> mClipChildren;


    boolean mIsNeedCliped = false;
    Camera mCamera = null;
    Matrix matrix3D = null;

    private int[] mCLipCountInXY;
    private static final PointF sPointF = new PointF();

    private static final float NONZERO_EPSILON = .001f;

    public void setPageTransformationInfo(PageTransformationInfo info){
        this.mPageTransformationInfo = info;
        this.mPageTransformationInfo.setClipViews(mClipChildren);
        this.mPageTransformationInfo.setPagedView(mView.getNotClipView());
    }


    public void setPosition(float x,float y){
        System.out.println("mbValid:"+ isValid+"  mbPagPe: "+isPage+" x: "+x+"  y: "+y);
        if(isValid){
            this.x = x;
            this.y = y;
            if(isPage){
                mTranslation.set(x,y);
            }else{
                mTranslation.set(x-mView.getX(),y-mView.getY());
            }
        }
    }

    public void setPageViews(View pageView, boolean needClip, int[] countXY){
        setView(new NotClipView(pageView),true);
        mIsNeedCliped = needClip;
        mCLipCountInXY = countXY;
        if(mIsNeedCliped && pageView != null){
            int width = pageView.getWidth();
            int height= pageView.getHeight();
            int columnCount = mCLipCountInXY[0];
            int rowCount = mCLipCountInXY[1];
            int widthStep = width / columnCount;
            int heightStep = height / rowCount;
            if (mClipChildren == null) {
                mClipChildren = new ArrayList<>(rowCount * columnCount);
            } else {
                mClipChildren.clear();
            }
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    ClipView view = new ClipView();
                    view.setColumnIndex(i);
                    view.setRowIndex(j);
                    view.getDisRect().set(i * widthStep, j * heightStep, (i + 1)
                            * widthStep, (j + 1) * heightStep);
                    mClipChildren.add(view);
                }
            }

        }
    }

    public void setView(ICompatibleView view, boolean isPage) {
        mView = view;
        if (view != null) {
            isValid = true;
            setRotation(view.getWidth() / 2,view.getHeight() / 2);

            x = mView.getX();
            y = mView.getY();

        } else {
            isValid = false;
        }
        this.isPage = isPage;
        mTranslation.set(0, 0);
        mRotationDegreeX = 0;
        mRotationDegreeY = 0;
        mRotationDegreeZ = 0;
    }

    public void setRotation(float x,float y){
        this.mRotationX = x;
        this.mRotationY = y;
    }

    public void setRotationZ(float z) {
        this.mRotationZ = z;
    }


    public void setRotationDegreex(float x, float y, float z) {
        mRotationDegreeX = x;
        mRotationDegreeY = y;
        mRotationDegreeZ = z;
    }

    public float getRotationDegreeX() {
        return mRotationDegreeX;
    }

    public void setRotationDegreeX(float rotationDegreeX) {
        this.mRotationDegreeX = rotationDegreeX;
    }

    public float getRotationDegreeY() {
        return mRotationDegreeY;
    }

    public void setRotationDegreeY(float rotationDegreeY) {
        this.mRotationDegreeY = rotationDegreeY;
    }

    public float getRotationDegreeZ() {
        return mRotationDegreeZ;
    }

    public void setRotationDegreeZ(float rotationDegreeZ) {
        this.mRotationDegreeZ = rotationDegreeZ;
    }

    public void clearTransform() {
        if (isPage && isValid) {
            mView.setAlpha(1.0f);
            mView.setVisibility(View.VISIBLE);
            if (null != mPageTransformationInfo) {
                mPageTransformationInfo.resetAndrecyle();
            }
            if (null != mClipChildren) {
                mClipChildren.clear();
            }
        }
    }

    /**
     * 将各种属性应用到Matrix3矩阵中
     */
    public void endEffect() {
        if (isValid) {
            Matrix tmpMatrix = null;
            if (!isPage) {
                ClipView clip = null;
                if (mView instanceof ClipView) {
                    clip = (ClipView) mView;
                }
                if (clip != null) {
                    tmpMatrix = clip.getTransMatrix();
                }
            } else {
                final PageTransformationInfo info = mPageTransformationInfo;
                if (info == null) {
                    return;
                }
                tmpMatrix = info.getPagedTransMatrix();
                info.setBmIsMatrixDirty(true);
            }
            if (null == tmpMatrix) {
                return;
            }
            tmpMatrix.reset();
            if (!nonzero(mRotationDegreeX) && !nonzero(mRotationDegreeY)) {
                System.out.println("mTranslation: "+mTranslation.x+","+mTranslation.y);
                tmpMatrix.setTranslate(mTranslation.x, mTranslation.y);
                tmpMatrix.preRotate(mRotationDegreeZ, mRotationX, mRotationY);
                tmpMatrix.preScale(mScaleX, mScaleY, mRotationX, mRotationY);
            } else {
                if (mCamera == null) {
                    mCamera = new Camera();
                    matrix3D = new Matrix();
                }
                tmpMatrix.preScale(mScaleX, mScaleY, mRotationX, mRotationY);
                mCamera.save();
                if (mCameraDistance != -8) {
                    mCamera.setLocation(0, 0, mCameraDistance);
                }
                mCamera.translate(0, 0, -mRotationZ);
                mCamera.rotate(0, mRotationDegreeY, -mRotationDegreeZ);
                mCamera.rotateX(-mRotationDegreeX);
                mCamera.translate(0, 0, mRotationZ);
                mCamera.getMatrix(matrix3D);
                matrix3D.preTranslate(-mRotationX, -mRotationY);
                matrix3D.postTranslate(mRotationX + mTranslation.x, mRotationY
                        + mTranslation.y);
                tmpMatrix.postConcat(matrix3D);
                mCamera.restore();
            }
        }
    }

    private static boolean nonzero(float value) {
        return (value < -NONZERO_EPSILON || value > NONZERO_EPSILON);
    }


    public float getHeight() {
        if (isValid) {
            return mView.getHeight();
        }
        return 0;
    }

    public float getWidth() {
        if (isValid) {
            return mView.getWidth();
        }
        return 0;
    }

    public void setAlpha(float alpha) {
        if (isValid) {
            if (!mIsNeedCliped) {
                mView.setAlpha(alpha);
            } else {
                mPageTransformationInfo.setAlpha(alpha);
            }
        }
    }

    public void setScale(float scale_x, float scale_y) {
        this.mScaleX = scale_x;
        this.mScaleY = scale_y;
    }


    public void setVisible(boolean isVisible) {
        if (isValid) {
            if (isVisible) {
                mView.setVisibility(View.VISIBLE);
            } else {
                mView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public ViewEffect3D getChildAt(int index) {
        if (isValid && isPage) {
            ViewEffect3D vg = new ViewEffect3D();
            if (mClipChildren != null && index >= 0
                    && index <= mClipChildren.size()) {
                vg.setView(mClipChildren.get(index), false);
                return vg;
            }
        }
        return null;
    }

    public int getChildCount() {
        if (isValid && isPage) {
            if (mClipChildren != null) {
                return mClipChildren.size();
            }
        }
        return 0;
    }

    public int getCellCountX() {
        if (isValid && isPage) {
            return mCLipCountInXY[0];
        }
        return 0;
    }

    public int getCellCountY() {
        if (isValid && isPage) {
            return mCLipCountInXY[1];
        }
        return 0;
    }

    public int getColumnNum() {
        if (isValid) {
            return mView.getColumnNum();
        }
        return 0;
    }

    public int getRowNum() {
        if (isValid) {
            return mView.getRowNum();
        }
        return 0;
    }

    public void setDrawOrder(boolean clockwize) {
        if (null != mPageTransformationInfo) {
            mPageTransformationInfo.setBmDrawOrder(clockwize);
        }
    }

    public void setCameraDistance(float distance) {
        mCameraDistance = distance;
    }

    public PointF getTag() {
        sPointF.x = mView.getX();
        sPointF.y = mView.getY();
        return sPointF;
    }

    public void setRotationAngle(float x, float y, float z) {
        mRotationDegreeX = x;
        mRotationDegreeY= y;
        mRotationDegreeZ = z;
    }

}
