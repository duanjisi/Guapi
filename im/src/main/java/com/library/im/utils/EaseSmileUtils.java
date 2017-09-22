/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.library.im.utils;

import android.content.Context;
import android.net.Uri;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.library.im.controller.EaseUI;
import com.library.im.domain.EaseEmojicon;
import com.library.im.model.EaseDefaultEmojiconDatas;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EaseSmileUtils {
    public static final String DELETE_KEY = "em_delete_delete_expression";

    public static final String f_static_000 = "[呲牙]";
    public static final String f_static_001 = "[调皮]";
    public static final String f_static_002 = "[流汗]";
    public static final String f_static_003 = "[偷笑]";
    public static final String f_static_004 = "[再见]";
    public static final String f_static_005 = "[敲打]";
    public static final String f_static_006 = "[擦汗]";
    public static final String f_static_007 = "[猪头]";
    public static final String f_static_008 = "[玫瑰]";
    public static final String f_static_009 = "[流泪]";
    public static final String f_static_010 = "[大哭]";
    public static final String f_static_011 = "[嘘]";
    public static final String f_static_012 = "[酷]";
    public static final String f_static_013 = "[抓狂]";
    public static final String f_static_014 = "[委屈]";
    public static final String f_static_015 = "[便便]";
    public static final String f_static_016 = "[炸弹]";
    public static final String f_static_017 = "[菜刀]";
    public static final String f_static_018 = "[可爱]";
    public static final String f_static_019 = "[色]";
    public static final String f_static_020 = "[害羞]";
    public static final String f_static_021 = "[得意]";
    public static final String f_static_022 = "[吐]";
    public static final String f_static_023 = "[微笑]";
    public static final String f_static_024 = "[发怒]";
    public static final String f_static_025 = "[尴尬]";
    public static final String f_static_026 = "[惊恐]";
    public static final String f_static_027 = "[冷汗]";
    public static final String f_static_028 = "[爱心]";
    public static final String f_static_029 = "[示爱]";
    public static final String f_static_030 = "[白眼]";
    public static final String f_static_031 = "[傲慢]";
    public static final String f_static_032 = "[难过]";
    public static final String f_static_033 = "[惊讶]";
    public static final String f_static_034 = "[疑问]";
    public static final String f_static_035 = "[睡]";
    public static final String f_static_036 = "[亲亲]";
    public static final String f_static_037 = "[憨笑]";
    public static final String f_static_038 = "[爱情]";
    public static final String f_static_039 = "[衰]";
    public static final String f_static_040 = "[撇嘴]";
    public static final String f_static_041 = "[阴险]";
    public static final String f_static_042 = "[奋斗]";
    public static final String f_static_043 = "[发呆]";
    public static final String f_static_044 = "[右哼哼]";
    public static final String f_static_045 = "[拥抱]";
    public static final String f_static_046 = "[坏笑]";
    public static final String f_static_047 = "[飞吻]";
    public static final String f_static_048 = "[鄙视]";
    public static final String f_static_049 = "[晕]";
    public static final String f_static_050 = "[大兵]";
    public static final String f_static_051 = "[可怜]";
    public static final String f_static_052 = "[强]";
    public static final String f_static_053 = "[弱]";
    public static final String f_static_054 = "[握手]";
    public static final String f_static_055 = "[胜利]";
    public static final String f_static_056 = "[抱拳]";
    public static final String f_static_057 = "[凋谢]";
    public static final String f_static_058 = "[饭]";
    public static final String f_static_059 = "[蛋糕]";
    public static final String f_static_060 = "[西瓜]";
    public static final String f_static_061 = "[啤酒]";
    public static final String f_static_062 = "[飘虫]";
    public static final String f_static_063 = "[勾引]";
    public static final String f_static_064 = "[OK]";
    public static final String f_static_065 = "[爱你]";
    public static final String f_static_066 = "[咖啡]";
    public static final String f_static_067 = "[钱]";
    public static final String f_static_068 = "[月亮]";
    public static final String f_static_069 = "[美女]";
    public static final String f_static_070 = "[刀]";
    public static final String f_static_071 = "[发抖]";
    public static final String f_static_072 = "[差劲]";
    public static final String f_static_073 = "[拳头]";
    public static final String f_static_074 = "[心碎]";
    public static final String f_static_075 = "[太阳]";
    public static final String f_static_076 = "[礼物]";
    public static final String f_static_077 = "[足球]";
    public static final String f_static_078 = "[骷髅]";
    public static final String f_static_079 = "[挥手]";
    public static final String f_static_080 = "[闪电]";
    public static final String f_static_081 = "[饥饿]";
    public static final String f_static_082 = "[困]";
    public static final String f_static_083 = "[咒骂]";
    public static final String f_static_084 = "[折磨]";
    public static final String f_static_085 = "[抠鼻]";
    public static final String f_static_086 = "[鼓掌]";
    public static final String f_static_087 = "[糗大了]";
    public static final String f_static_088 = "[左哼哼]";
    public static final String f_static_089 = "[哈欠]";
    public static final String f_static_090 = "[快哭了]";
    public static final String f_static_091 = "[吓]";
    public static final String f_static_092 = "[篮球]";
    public static final String f_static_093 = "[乒乓球]";
    public static final String f_static_094 = "[NO]";
    public static final String f_static_095 = "[跳跳]";
    public static final String f_static_096 = "[怄火]";
    public static final String f_static_097 = "[转圈]";
    public static final String f_static_098 = "[磕头]";
    public static final String f_static_099 = "[回头]";
    public static final String f_static_100 = "[跳绳]";
    public static final String f_static_101 = "[激动]";
    public static final String f_static_102 = "[街舞]";
    public static final String f_static_103 = "[献吻]";
    public static final String f_static_104 = "[左太极]";
    public static final String f_static_105 = "[右太极]";
    public static final String f_static_106 = "[闭嘴]";

    private static final Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Object> emoticons = new HashMap<Pattern, Object>();


    static {
        EaseEmojicon[] emojicons = EaseDefaultEmojiconDatas.getData();
        for (int i = 0; i < emojicons.length; i++) {
            addPattern(emojicons[i].getEmojiText(), emojicons[i].getIcon());
        }
        EaseUI.EaseEmojiconInfoProvider emojiconInfoProvider = EaseUI.getInstance().getEmojiconInfoProvider();
        if (emojiconInfoProvider != null && emojiconInfoProvider.getTextEmojiconMapping() != null) {
            for (Entry<String, Object> entry : emojiconInfoProvider.getTextEmojiconMapping().entrySet()) {
                addPattern(entry.getKey(), entry.getValue());
            }
        }

    }

    /**
     * 添加文字表情mapping
     * @param emojiText emoji文本内容
     * @param icon 图片资源id或者本地路径
     */
    public static void addPattern(String emojiText, Object icon) {
        emoticons.put(Pattern.compile(Pattern.quote(emojiText)), icon);
    }


    /**
     * replace existing spannable with smiles
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Object value = entry.getValue();
                    if (value instanceof String && !((String) value).startsWith("http")) {
                        File file = new File((String) value);
                        if (!file.exists() || file.isDirectory()) {
                            return false;
                        }
                        spannable.setSpan(new ImageSpan(context, Uri.fromFile(file)),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        spannable.setSpan(new ImageSpan(context, (Integer) value),
                                matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Object> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

    public static int getSmilesSize() {
        return emoticons.size();
    }


}
