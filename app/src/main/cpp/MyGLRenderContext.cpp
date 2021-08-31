//
// Created by 203 on 2021/8/31.
//

#include "MyGLRenderContext.h"
#include "util/LogUtil.h"
#include "util/ImageDef.h"


MyGLRenderContext *MyGLRenderContext::m_pContext = nullptr;

MyGLRenderContext::MyGLRenderContext() = default;


MyGLRenderContext::~MyGLRenderContext() {

};

void MyGLRenderContext::SetImageData(int format, int width, int height, uint8_t *pData) {
    LOGCATE("MyGLRenderContext::SetImageData format=%d, width=%d, height=%d, pData=%p", format,
            width, height, pData);
    NativeImage nativeImage;
    nativeImage.format = format;
    nativeImage.width = width;
    nativeImage.height = height;
    nativeImage.ppPlane[0] = pData;

    switch (format) {
        case IMAGE_FORMAT_NV12:
        case IMAGE_FORMAT_NV21:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + width * height;
            break;
        case IMAGE_FORMAT_I420:
            nativeImage.ppPlane[1] = nativeImage.ppPlane[0] + width * height;
            nativeImage.ppPlane[2] = nativeImage.ppPlane[1] + width * height / 4;
            break;
        default:
            break;

    }
}

void MyGLRenderContext::OnSurfaceCreated() {
    LOGCATE("MyGLRenderContext::OnSurfaceCreated");
    glClearColor(1.0f,1.0f,0.5f,1.0f);
    m_Sample->Init();
}