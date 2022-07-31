package com.example.nestedviewoverdraw.overdraw_custom_view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * A custom view that displays a stacked collection of droid cards. See onDraw() for use of a
 * clipRect() to restrict the drawing area to avoid partial overdraws.
 */
class CardsView extends View {
    /**
     * The list of droids displayed in this view.
     */
    private CardModel[] mCardModels;

    /**
     * The width of the droid image. In this sample, we hard code an image width in
     * DroidCardsActivity and pass it as an argument to this view.
     */
    float mDroidImageWidth;

    /**
     * The distance between the left edges of two adjacent cards. The cards overlap horizontally.
     */
    protected float mCardSpacing;

    /**
     * Keeps track of the left coordinate for each card.
     */
    private float mCardLeft;

    /**
     * A list of DroidCards objected. We use Asynctasks to populate the contents of this list. See
     * the DroidCardWorkerTask class that extends AsyncTask.
     */
    private ArrayList<Card> mDroidCards = new ArrayList<Card>();

    /**
     *
     * @param context           The app context.
     * @param cardModels            The Droid objects associated with DroidCards.
     * @param droidImageWidth   The width of each Droid image. Hardcoded in DroidCardsActivity.
     * @param cardSpacing       The distance between the left edges of two adjacent cards.
     */
    public CardsView(Context context, CardModel[] cardModels, float droidImageWidth,
                     float cardSpacing) {
        super(context);

        mCardModels = cardModels;
        mDroidImageWidth = droidImageWidth;
        mCardSpacing = cardSpacing;

        // Fire AsyncTasks to fetch and scale the bitmaps.
        for (int i = 0; i < mCardModels.length; i++) {
            new DroidCardWorkerTask().execute(mCardModels[i]);
        }
    }

    /**
     * Custom implementation to do drawing in this view. Waits for the AsyncTasks to fetch
     * bitmaps for each Droid and populate mDroidCards, a list of DroidCard objects. Then, draws
     * overlapping droid cards.
     */
    protected void onDraw(Canvas canvas) {
        // Don't draw anything until all the Asynctasks are done and all the DroidCards are ready.
        if (mCardModels.length > 0 && mDroidCards.size() == mCardModels.length) {
            // Loop over all the droids, except the last one.
            int i;
            for (i = 0; i < mDroidCards.size() - 1; i++) {

                // Each card is laid out a little to the right of the previous one.
                mCardLeft = i * mCardSpacing;

                // Save the canvas state.
                canvas.save();

                // Restrict the drawing area to only what will be visible.
                canvas.clipRect(
                        mCardLeft,
                        0,
                        mCardLeft + mCardSpacing,
                        mDroidCards.get(i).getHeight()
                );

                // Draw the card. Only the parts of the card that lie within the bounds defined by
                // the clipRect() get drawn.
                drawDroidCard(canvas, mDroidCards.get(i), mCardLeft, 0);

                // Revert canvas to non-clipping state.
                canvas.restore();
            }

            // Draw the final card. This one doesn't get clipped.
            drawDroidCard(canvas, mDroidCards.get(mDroidCards.size() - 1),
                    mCardLeft + mCardSpacing, 0);
        }

        // Invalidate the whole view. Doing this calls onDraw() if the view is visible.
        invalidate();
    }

    /**
     * Draws a droid card to a canvas at the specified position.
     */
    protected void drawDroidCard(Canvas canvas, Card droidCard, float left, float top) {
        Paint paint = new Paint();
        Bitmap bm = droidCard.getBitmap();
        CardModel cardModel = droidCard.getDroid();

        // Draw outer rectangle.
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        Rect cardRect = new Rect(
                (int)left,
                (int)top,
                (int)left + (int) droidCard.getWidth(),
                (int)top + (int) droidCard.getHeight()
        );
        canvas.drawRect(cardRect, paint);

        // Draw border.
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(cardRect, paint);

        // Draw the bitmap centered in the card body.
        canvas.drawBitmap(
                bm,
                (cardRect.left + (droidCard.getWidth() / 2)) - (bm.getWidth() / 2),
                (cardRect.top + (int) droidCard.getHeaderHeight() + (droidCard.getBodyHeight() / 2)
                        - (bm.getHeight() / 2)),
                null
        );

        // Write the droid's name in the header.
        paint.setTextSize(droidCard.getTitleSize());
        paint.setColor(getResources().getColor(cardModel.getColor()));
        paint.setStyle(Paint.Style.STROKE);

        // Calculate the the left and top offsets for the title.
        int titleLeftOffset = cardRect.left + (int) droidCard.getTitleXOffset();
        int titleTopOffset = cardRect.top + (int) droidCard.getTitleYOffset() +
                (int) droidCard.getTitleSize();

        // Draw the title text.
        canvas.drawText(cardModel.getName(), titleLeftOffset, titleTopOffset, paint);
    }

    /**
     * Creates and returns a bitmap from a drawable resource.
     */
    public Bitmap makeBitmap(Resources res, int resId, int reqWidth) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize.
        options.inSampleSize = calculateInSampleSize(options, reqWidth);

        // Decode bitmap with inSampleSize set.
        options.inJustDecodeBounds = false;

        // Decode bitmap.
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Returns a bitmap scaled to a specific width.
     */
    private Bitmap getScaledBitmap(Bitmap bitmap, float width) {
        float scale = width / bitmap.getWidth();
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale),
                (int) (bitmap.getHeight() * scale), false);
    }

    /**
     * Requests the decoder to subsample the original image, possibly returning a smaller image to
     * save memory.
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth) {
        // Get the raw width of image.
        final int width = options.outWidth;
        int inSampleSize = 1;

        // Calculate the best inSampleSize.
        if (width > reqWidth) {
            final int halfWidth = width / 2;
            while ((halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Worker that fetches bitmaps in the background and stores them in a list of DroidCards.
     * The order of the
     */
    class DroidCardWorkerTask extends AsyncTask<CardModel, Void, Bitmap> {
        CardModel cardModel;
        private final WeakReference<ArrayList<Card>> mDroidCardsReference;

        public DroidCardWorkerTask() {
            mDroidCardsReference = new WeakReference<ArrayList<Card>>(mDroidCards);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(CardModel... params) {
            cardModel = params[0];
            // Scale the bitmap.
            return getScaledBitmap(
                    makeBitmap(getResources(), cardModel.getAvatarId(), (int) mDroidImageWidth),
                    mDroidImageWidth
            );
        }

        /**
         * Creates a DroidCard using the retrieved bitmap and stores it in a DroidCards list.
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // Check that the list and bitmap are not null.
            if (mDroidCardsReference != null && bitmap != null) {
                // Create a new DroidCard.
                mDroidCards.add(new Card(cardModel, bitmap));
            }
        }
    }
}