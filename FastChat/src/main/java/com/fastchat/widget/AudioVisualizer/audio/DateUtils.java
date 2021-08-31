/*
 * Copyright (C) 2014 Open Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.fastchat.widget.AudioVisualizer.audio;

import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import android.text.format.DateFormat;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility methods to help display dates in a nice, easily readable way.
 */
public class DateUtils extends android.text.format.DateUtils {

  @SuppressWarnings("unused")
  private static final String           TAG         = DateUtils.class.getSimpleName();

  private static boolean isWithin(final long millis, final long span, final TimeUnit unit) {
    return System.currentTimeMillis() - millis <= unit.toMillis(span);
  }

  private static boolean isYesterday(final long when) {
    return DateUtils.isToday(when + TimeUnit.DAYS.toMillis(1));
  }

  private static int convertDelta(final long millis, TimeUnit to) {
    return (int) to.convert(System.currentTimeMillis() - millis, TimeUnit.MILLISECONDS);
  }

  private static String getFormattedDateTime(long time, String template, Locale locale) {
    final String localizedPattern = getLocalizedPattern(template, locale);
    String ret = new SimpleDateFormat(localizedPattern, locale).format(new Date(time));

    // having ".," in very common and known abbreviates as weekdays or month names is not needed,
    // looks ugly and makes the string longer than needed
    ret = ret.replace(".,", ",");

    return ret;
  }



  public static String getExtendedRelativeTimeSpanString(final Context c, final Locale locale, final long timestamp) {

      return "";

  }

  public static String getRelativeDate(@NonNull Context context,
                                       @NonNull Locale locale,
                                       long timestamp)
  {
    return "";
  }

  private static String getLocalizedPattern(String template, Locale locale) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      return DateFormat.getBestDateTimePattern(locale, template);
    } else {
      return new SimpleDateFormat(template, locale).toLocalizedPattern();
    }
  }

  public static String getFormatedDuration(long millis) {
    return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis),
            TimeUnit.MILLISECONDS.toSeconds(millis-(TimeUnit.MILLISECONDS.toMinutes(millis)*60000)));
  }


}