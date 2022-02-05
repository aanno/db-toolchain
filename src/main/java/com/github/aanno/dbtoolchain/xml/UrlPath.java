package com.github.aanno.dbtoolchain.xml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class UrlPath {

    private final List<String> path;

    public UrlPath(String url) {
        path = Arrays.asList(url.split("[/]"));
    }

    UrlPath(List<String> path) {
        // defensive copy
        this.path = Collections.unmodifiableList(new ArrayList<>(path));
    }

    List<String> getInternal() {
        return path;
    }

    public int size() {
        return path.size();
    }

    public UrlPath append(UrlPath path) {
        List<String> newPath = new ArrayList<>(this.path);
        newPath.addAll(path.getInternal());
        return new UrlPath(newPath);
    }

    public UrlPath append(String path) {
        return append(new UrlPath(path));
    }

    public UrlPath descend(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("level " + level);
        } else if (level == 0) {
            return this;
        } else {
            int size = path.size();
            return new UrlPath(path.subList(0, size - level + 1));
        }
    }

    public UrlPath resolve(UrlPath path) {
        // defensive copy
        List<String> newPath = new ArrayList<>(this.path);
        int size = path.size();
        for (int i = 0; i < size; ++i) {
            String current = path.getInternal().get(i).trim();
            if (".".equals(current)) {
                continue;
            } else if ("..".equals(current)) {
                newPath = newPath.subList(0, newPath.size() - 1);
            } else {
                newPath.add(current);
            }
        }
        return new UrlPath(newPath);
    }

    public UrlPath resolve(String path) {
        return resolve(new UrlPath(path));
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner("/");
        int size = path.size();
        for (int i = 0; i < size; ++i) {
            joiner.add(path.get(i));
        }
        return joiner.toString();
    }

    public static void main(String[] args) throws Exception {
        UrlPath path = new UrlPath("file://mnt/home/tpasch/scm/aanno/nextcloud-docker/.examples/docker-compose/with-nginx-proxy-self-signed-ssl/mariadb/fpm");
        UrlPath path2 = path.resolve(".././1st");
        System.out.println("path: " + path + " path2: " + path2);
    }

}
