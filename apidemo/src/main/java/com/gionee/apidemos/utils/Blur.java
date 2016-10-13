package com.gionee.apidemos.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class Blur {

    public void blur(Bitmap bitmap, int[] srcData, int bitOfPic, int width, int height, int ratio) {
        int quarterW = width >> 2;
        int quarterH = height >> 2;
        int[] temp = new int[quarterW * quarterH];
        Log.d("GioneeBlur", "temp = " + temp.length);
        Log.d("GioneeBlur", "srcData = " + srcData.length);

        for (int i = 0; i < quarterH; i++) {
            for (int j = 0; j < quarterW; j++) {
                temp[i * quarterW + j] = srcData[4 * i * width + 4 * j];
            }
        }

        filter_x(temp, bitOfPic, quarterW, quarterH, ratio);
        filter_y(temp, bitOfPic, quarterW, quarterH, ratio);
        int pixels[] = new int[width * height];

        for (int i = 0; i < quarterH; i++) {
            for (int j = 0; j < quarterW; j++) {
                int rgba = temp[i * quarterW + j];
                int first = 4 * i * width + 4 * j;
                pixels[first] = rgba;
                pixels[first + 1] = rgba;
                pixels[first + 2] = rgba;
                pixels[first + 3] = rgba;

                int second = first + width;
                pixels[second] = rgba;
                pixels[second + 1] = rgba;
                pixels[second + 2] = rgba;
                pixels[second + 3] = rgba;

                int third = second + width;
                pixels[third] = rgba;
                pixels[third + 1] = rgba;
                pixels[third + 2] = rgba;
                pixels[third + 3] = rgba;

                int fourth = third + width;
                pixels[fourth] = rgba;
                pixels[fourth + 1] = rgba;
                pixels[fourth + 2] = rgba;
                pixels[fourth + 3] = rgba;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    void filter_x(int[] pix, int pixL, int w, int h, int level) {
        int k, n, n2;
        int tmp1, tmp2, tmp3, tmp4;
        int[] result;
        int bitShift, maskB, maskG, maskR, maskA;

        result = new int[w * h];
        switch (pixL) {
            case 24:

                switch (level) {
                    case 1:
                        n = 4;
                        n2 = 2;
                        bitShift = 2;
                        maskB = 0x3fc;
                        maskG = 0x3fc00;
                        maskR = 0x3fc0000;
                        maskA = 0x3fc0000;
                        break;
                    case 2:
                        n = 8;
                        n2 = 4;
                        bitShift = 3;
                        maskB = 0x7f8;
                        maskG = 0x7f800;
                        maskR = 0x7f80000;
                        maskA = 0x7f80000;
                        break;
                    case 3:
                        n = 16;
                        n2 = 8;
                        bitShift = 4;
                        maskB = 0xff0;
                        maskG = 0xff000;
                        maskR = 0xff00000;
                        maskA = 0xff00000;
                        break;
                    case 4:
                        n = 32;
                        n2 = 16;
                        bitShift = 5;
                        maskB = 0x1fe0;
                        maskG = 0x1fe000;
                        maskR = 0x1fe00000;
                        maskA = 0x1fe00000;
                        break;
                    case 5:
                        n = 64;
                        n2 = 32;
                        bitShift = 6;
                        maskB = 0x3fc0;
                        maskG = 0x3fc000;
                        maskR = 0x3fc00000;
                        maskA = 0x3fc00000;
                        break;
                    default:
                        return;
                }

                tmp1 = 0;
                tmp2 = 0;
                tmp3 = 0;
                tmp4 = 0;
                // 半径
                for (k = 0; k < n; k++) {
                    tmp1 += (0xff & pix[k]);
                    tmp2 += (0xff00 & pix[k]);
                    tmp3 += (0xff0000 & pix[k]);
                    tmp4 += ((0xff000000 & pix[k]) >> 8);
                }

                for (k = 0; k < n2; k++) {
                    tmp1 -= (0xff & pix[k]);
                    tmp2 -= (0xff00 & pix[k]);
                    tmp3 -= (0xff0000 & pix[k]);
                    tmp4 -= ((0xff000000 & pix[k]) >> 8);

                    tmp1 += (0xff & pix[k + n2]);
                    tmp2 += (0xff00 & pix[k + n2]);
                    tmp3 += (0xff0000 & pix[k + n2]);
                    tmp4 += ((0xff000000 & pix[k + n2]) >> 8);
                    result[k] = ((((tmp1 & maskB) >> bitShift) | ((tmp2 & maskG) >> bitShift)
                            | ((tmp3 & maskR) >> bitShift) | ((tmp4 & maskA) << (8 - bitShift))));
                }

                for (k = n2; k < w * h - n2; k++) {
                    tmp1 -= (0xff & pix[k - n2 + 1]);
                    tmp2 -= (0xff00 & pix[k - n2 + 1]);
                    tmp3 -= (0xff0000 & pix[k - n2 + 1]);
                    tmp4 -= ((0xff000000 & pix[k - n2 + 1]) >> 8);

                    tmp1 += (0xff & pix[k + n2]);
                    tmp2 += (0xff00 & pix[k + n2]);
                    tmp3 += (0xff0000 & pix[k + n2]);
                    tmp4 += ((0xff000000 & pix[k + n2]) >> 8);
                    result[k] = ((((tmp1 & maskB) >> bitShift) | ((tmp2 & maskG) >> bitShift)
                            | ((tmp3 & maskR) >> bitShift) | ((tmp4 & maskA) << (8 - bitShift))));
                }

                for (k = w * h - n2; k < w * h; k++) {
                    tmp1 += (0xff & pix[w * h - 1]);
                    tmp2 += (0xff00 & pix[w * h - 1]);
                    tmp3 += (0xff0000 & pix[w * h - 1]);
                    tmp4 += ((0xff0000 & pix[w * h - 1]) >> 8);

                    tmp1 -= (0xff & pix[k - n2 + 1]);
                    tmp2 -= (0xff00 & pix[k - n2 + 1]);
                    tmp3 -= (0xff0000 & pix[k - n2 + 1]);
                    tmp4 -= ((0xff0000 & pix[k - n2 + 1]) >> 8);
                    result[k] = ((((tmp1 & maskB) >> bitShift) | ((tmp2 & maskG) >> bitShift)
                            | ((tmp3 & maskR) >> bitShift) | ((tmp4 & maskA) << (8 - bitShift))));
                }

                break;

            default:
                return;
        }
    }

    void filter_y(int[] pix, int pixL, int w, int h, int level) {
        int i, j;
        int[] result = new int[w * h];

        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                result[j * h + i] = pix[i * w + j];
            }
        }

        filter_x(result, pixL, h, w, level);

        for (i = 0; i < h; i++) {
            for (j = 0; j < w; j++) {
                pix[i * w + j] = result[j * h + i];
            }
        }
    }
}
