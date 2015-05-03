package com.babystep.tedtalks;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 03.05.2015.
 */
public class RssParser {

    private static final String LOG_TAG = RssParser.class.getSimpleName();

    private static final String ns = null;

    public List<Item> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readRss(parser);
        } finally {
            in.close();
        }
    }

    private List<Item> readRss(XmlPullParser parser) throws IOException, XmlPullParserException {
        List<Item> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("channel")) {
                return readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private List<Item> readChannel(XmlPullParser parser)
            throws IOException, XmlPullParserException {

        List<Item> entries = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("item")) {
                entries.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private Item readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        String title = null;
        String link = null;
        String description = null;
        String pubDate = null;
        String mediaContentURL = null;
        String mediaThumbnailURL = null;
        String Author = null;
        String Duration = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            //Log.d(LOG_TAG, "Tag name = " + name);

            switch (name) {
                case "title":
                    title = readTitle(parser);
                    break;
                case "link":
                    link = readLink(parser);
                    break;
                case "itunes:summary":
                    description = readDescription(parser);
                    break;
                case "pubDate":
                    pubDate = readPubDate(parser);
                    break;
                case "media:content":
                    mediaContentURL = readMediaContentURL(parser);
                    break;
                case "itunes:author":
                    Author = readAuthor(parser);
                    break;
                case "itunes:duration":
                    Duration = readDuration(parser);
                    break;
                case "media:thumbnail":
                    mediaThumbnailURL = readMediaThumbnailURL(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }

        return new Item(title, link, description, pubDate, mediaContentURL, mediaThumbnailURL, Author, Duration);
    }

    private String readMediaContentURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        String attrValue = parser.getAttributeValue(ns, "url");
        readRequiredTag(parser, "media:content");
        return attrValue;
    }

    private String readMediaThumbnailURL(XmlPullParser parser) throws IOException, XmlPullParserException {
        String attrValue = parser.getAttributeValue(ns, "url");
        readRequiredTag(parser, "media:thumbnail");
        return attrValue;
    }

    private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readRequiredTag(parser, "itunes:author");
    }

    private String readDuration(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readRequiredTag(parser, "itunes:duration");
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readRequiredTag(parser, "pubDate");
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readRequiredTag(parser, "itunes:summary");
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readRequiredTag(parser, "link");
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        return readRequiredTag(parser, "title");
    }

    private String readRequiredTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return result;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    public static class Item implements Serializable {
        public final String title;
        public final String link;
        public final String description;
        public final String pubDate;
        public final String mediaContentURL;
        public final String mediaThumbnailURL;
        public final String Author;
        public final String Duration;

        public Item(String title, String link, String description, String pubDate, String mediaContentURL, String mediaThumbnailURL, String Author, String Duration) {
            this.title = title;
            this.link = link;
            this.description = description;
            this.pubDate = pubDate;
            this.mediaContentURL = mediaContentURL;
            this.mediaThumbnailURL = mediaThumbnailURL;
            this.Author = Author;
            this.Duration = Duration;
        }

        @Override
        public String toString() {
            return "{" + title + "," + link + "," + description + "," + pubDate + "," + mediaContentURL + "," + Author + "," + Duration + "}";
        }
    }
}
