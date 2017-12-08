package com.wonderkiln.camerakit;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.wonderkiln.camerakit.core.R;

class TextureViewPreview extends PreviewImpl {
	private final SurfaceViewContainer mContainer;
	private final TextureView mTextureView;
	private int mDisplayOrientation;

	TextureViewPreview(Context context, ViewGroup parent) {
		final View view = View.inflate(context, R.layout.texture_view, parent);

		mContainer = view.findViewById(R.id.texture_view_container);
		mContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
			@Override
			public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
				setSize(mContainer.getWidth(), mContainer.getHeight());
			}
		});
		mTextureView = (TextureView) view.findViewById(R.id.texture_view);

		mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int w, int h) {
				setSize(w, h);
				mContainer.setPreviewSize(new Size(w, h));
				mTextureView.getSurfaceTexture().setDefaultBufferSize(w, h);
				configureTransform();
				dispatchSurfaceChanged();
			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int w, int h) {
				setSize(w, h);
				configureTransform();
				dispatchSurfaceChanged();
			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
				setSize(0, 0);

				return true;
			}

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

			}
		});

	}

	@Override
	void setPreviewParameters(int width, int height, int format) {
		super.setPreviewParameters(width, height, format);

		if(isReady()){
			mContainer.setPreviewSize(new Size(width, height));
			mTextureView.getSurfaceTexture().setDefaultBufferSize(width, height);
		}
	}

	@Override
	Surface getSurface() {
		return new Surface(mTextureView.getSurfaceTexture());
	}

	@Override
	SurfaceTexture getSurfaceTexture() {
		return mTextureView.getSurfaceTexture();
	}

	@Override
	View getView() {
		return mTextureView;
	}

	@Override
	Class getOutputClass() {
		return SurfaceTexture.class;
	}

	@Override
	void setDisplayOrientation(int displayOrientation) {
		mDisplayOrientation = displayOrientation;
		configureTransform();
	}

	@Override
	boolean isReady() {
		return mTextureView.getSurfaceTexture() != null;
	}

	@Override
	float getX() {
		return mTextureView.getX();
	}

	@Override
	float getY() {
		return mTextureView.getY();
	}

	private void configureTransform() {
		Matrix matrix = new Matrix();
		if (mDisplayOrientation % 180 == 90) {
			final int width = getWidth();
			final int height = getHeight();
			// Rotate the camera preview when the screen is landscape.
			matrix.setPolyToPoly(
					new float[]{
							0.f, 0.f, // top left
							width, 0.f, // top right
							0.f, height, // bottom left
							width, height, // bottom right
					}, 0,
					mDisplayOrientation == 90 ?
							// Clockwise
							new float[]{
									0.f, height, // top left
									0.f, 0.f, // top right
									width, height, // bottom left
									width, 0.f, // bottom right
							} : // mDisplayOrientation == 270
							// Counter-clockwise
							new float[]{
									width, 0.f, // top left
									width, height, // top right
									0.f, 0.f, // bottom left
									0.f, height, // bottom right
							}, 0,
					4);
		}
		mTextureView.setTransform(matrix);
	}
}
