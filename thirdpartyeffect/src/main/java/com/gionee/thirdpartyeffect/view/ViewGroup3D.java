
package com.gionee.thirdpartyeffect.view;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.View;
import com.gionee.thirdpartyeffect.view.EasyViewGroup.PageTranformationInfo;

/**
 * 完成view 多种属性的设置，例如 translateX, translateY, Rotate,RotateX,RotateY等等。
 * 并将这些属性应用在Matrix3矩阵中。Matrix3矩阵将在绘制view时由绘制canvas使用。
 */
public class ViewGroup3D {

    private PageTranformationInfo mTranformationInfo;
    private ICompatibleView mView;
    private boolean mbValid = false;
    private boolean mbPage = false;

    /**
     * 位置坐标
     */
    private float x = 0f;
    private float y = 0f;
    // private float z = 0f;

    /**
     * 旋转角度
     */
    private float mRx = 0f;
    private float mRy = 0f;
    private float mRz = 0f;

    PointF mTranslation = new PointF();

    /**
     * 旋转点坐标
     */
    public float mOriginX = 0f;
    public float mOriginY = 0f;
    public float mOriginZ = 0f;

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

    public void SetPageSView(View pageView, boolean needClip, int[] countXY) {
        SetView(new NotClipView(pageView), true);
        mIsNeedCliped = needClip;
        mCLipCountInXY = countXY;
        if (mIsNeedCliped && pageView != null) {
            int width = pageView.getWidth();
            int height = pageView.getHeight();
            int columnCount = mCLipCountInXY[0];
            int rowCount = mCLipCountInXY[1];
            int widthStep = width / columnCount;
            int heightStep = height / rowCount;
            if (mClipChildren == null) {
                mClipChildren = new ArrayList<ClipView>(rowCount * columnCount);
            } else {
                mClipChildren.clear();
            }
            for (int i = 0; i < columnCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    ClipView view = new ClipView();
                    view.mColumnIndex = i;
                    view.mRowIndex = j;
                    view.mDisRect.set(i * widthStep, j * heightStep, (i + 1)
                            * widthStep, (j + 1) * heightStep);
                    mClipChildren.add(view);
                }
            }
        }
    }

    public void SetView(ICompatibleView view, boolean bPage) {
        mView = view;
        if (view != null) {
            mbValid = true;

            mOriginX = view.getWidth() / 2;
            mOriginY = view.getHeight() / 2;

            x = mView.getX();
            y = mView.getY();

        } else {
            mbValid = false;
        }
        mbPage = bPage;
        mTranslation.set(0, 0);
        mRx = 0;
        mRy = 0;
        mRz = 0;
    }

    void setPageTransformation(PageTranformationInfo info) {
        mTranformationInfo = info;
        mTranformationInfo.mClipViews = mClipChildren;
        mTranformationInfo.mPagedView = mView.getNotClipView();
    }

    public void setPosition(float x, float y) {
        System.out.println("mbValid:"+ mbValid+"  mbPage: "+mbPage+" x: "+x+"  y: "+y+"  mview.geX():"+mView.getX()+" getY:"+mView.getY());
        if (mbValid) {
            if (mbPage) {
                this.x = x;
                this.y = y;
                mTranslation.set(x, y);
            } else {
                this.x = x;
                this.y = y;
                mTranslation.set(x - mView.getX(), y - mView.getY());
            }
        }
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void setRotationX(float angle) {
        mRx = angle;
    }

    public void setRotationY(float angle) {
        mRy = angle;
    }

    public void setRotationZ(float angle) {
        mRz = angle;
    }

    public void setRotationAngle(float x, float y, float z) {
        mRx = x;
        mRy = y;
        mRz = z;
    }

    public PointF getTag() {
        sPointF.x = mView.getX();
        sPointF.y = mView.getY();
        return sPointF;
    }

    public float getWidth() {
        if (mbValid) {
            return mView.getWidth();
        }
        return 0;
    }

    private static boolean nonzero(float value) {
        return (value < -NONZERO_EPSILON || value > NONZERO_EPSILON);
    }

    /**
     * 将各种属性应用到Matrix3矩阵中
     */
    public void endEffect() {
        if (mbValid) {
            Matrix tmpMatrix = null;
            if (!mbPage) {
                ClipView clip = null;
                if (mView instanceof ClipView) {
                    clip = (ClipView) mView;
                }
                if (clip != null) {
                    tmpMatrix = clip.mTransMatrix;
                }
            } else {
                final PageTranformationInfo info = mTranformationInfo;
                if (info == null) {
                    return;
                }
                tmpMatrix = info.mPagedTransMatrix;
                info.bmIsMatrixDirty = true;
            }
            if (null == tmpMatrix) {
                return;
            }
            tmpMatrix.reset();
            if (!nonzero(mRx) && !nonzero(mRy)) {
                System.out.println("mTranslation: "+mTranslation.x+","+mTranslation.y);
                tmpMatrix.setTranslate(mTranslation.x, mTranslation.y);
                tmpMatrix.preRotate(mRz, mOriginX, mOriginY);
                tmpMatrix.preScale(mScaleX, mScaleY, mOriginX, mOriginY);
            } else {
                if (mCamera == null) {
                    mCamera = new Camera();
                    matrix3D = new Matrix();
                }
                System.out.println("mScaleX222: "+mScaleX+"  mScaleY:  "+mScaleY+" mOriginX:  "+mOriginX+" mOriginY:  "+mOriginY);
                tmpMatrix.preScale(mScaleX, mScaleY, mOriginX, mOriginY);
                mCamera.save();
                if (mCameraDistance != -8) {
                    mCamera.setLocation(0, 0, mCameraDistance);
                }
                mCamera.translate(0, 0, -mOriginZ);
                mCamera.rotate(0, mRy, -mRz);
                mCamera.rotateX(-mRx);
                mCamera.translate(0, 0, mOriginZ);
                mCamera.getMatrix(matrix3D);
                matrix3D.preTranslate(-mOriginX, -mOriginY);
                matrix3D.postTranslate(mOriginX + mTranslation.x, mOriginY
                        + mTranslation.y);
                tmpMatrix.postConcat(matrix3D);
                mCamera.restore();
            }
        }
    }

    public float getHeight() {
        if (mbValid) {
            return mView.getHeight();
        }
        return 0;
    }

    /**
     * 设置旋转点坐标X Y
     */
    public void setOrigin(float x, float y) {
        mOriginX = x;
        mOriginY = y;
    }

    /**
     * 设置旋转点z坐标
     */
    public void setOriginZ(float z) {
        mOriginZ = z;
    }

    public void clearTransform() {
        if (mbValid && mbPage) {
            mView.setAlpha(1.0f);
            mView.setVisibility(View.VISIBLE);
            if (null != mTranformationInfo) {
                mTranformationInfo.resetAndrecyle();
            }
            if (null != mClipChildren) {
                mClipChildren.clear();
            }
        }
    }

    public void setAlpha(float color_a) {
        if (mbValid) {
            if (!mIsNeedCliped) {
                mView.setAlpha(color_a);
            } else {
                mTranformationInfo.mAlpha = color_a;
            }
        }
    }

    public void setScale(float scale_x, float scale_y) {
        this.mScaleX = scale_x;
        this.mScaleY = scale_y;
    }


    public void setVisible(boolean b) {
        if (mbValid) {
            if (b) {
                mView.setVisibility(View.VISIBLE);
            } else {
                mView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public ViewGroup3D getChildAt(int index) {
        if (mbValid && mbPage) {
            ViewGroup3D vg = new ViewGroup3D();
            if (mClipChildren != null && index >= 0
                    && index <= mClipChildren.size()) {
                vg.SetView(mClipChildren.get(index), false);
                return vg;
            }
        }
        return null;
    }

    public int getChildCount() {
        if (mbValid && mbPage) {
            if (mClipChildren != null) {
                return mClipChildren.size();
            }
        }
        return 0;
    }

    public int getCellCountX() {
        if (mbValid && mbPage) {
            return mCLipCountInXY[0];
        }
        return 0;
    }

    public int getCellCountY() {
        if (mbValid && mbPage) {
            return mCLipCountInXY[1];
        }
        return 0;
    }

    public int getColumnNum() {
        if (mbValid) {
            return mView.getColumnNum();
        }
        return 0;
    }

    public int getRowNum() {
        if (mbValid) {
            return mView.getRowNum();
        }
        return 0;
    }

    public void setDrawOrder(boolean clockwize) {
        if (null != mTranformationInfo) {
            mTranformationInfo.bmDrawOrder = clockwize;
        }
    }

    public void setCameraDistance(float distance) {
        mCameraDistance = distance;
    }
}
