/*******************************************************************************
 * Copyright 2021 Itzbenz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package Ozone.Internal;

import Atom.Utility.Cache;
import Ozone.Manifest;
import arc.graphics.Pixmap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RepoCached extends Repo implements Module {
    public static HashMap<String, Pixmap> pixmapCache = new HashMap<>();
    
    public URL getResource(String s) {
        return Cache.tryCache(super.getResource(s));
    }
    
    public InputStream getResourceAsStream(String s) throws IOException {
        URL u = this.getResource(s);
        return u.openStream();
    }
    
    @Override
    public void init() throws Throwable {
        Repo rc = Manifest.getModule(Repo.class);
        assert rc != null;
        repos.addAll(rc.getRepos());
    }
    
    @Override
    public List<Class<? extends Module>> dependOnModule() throws IOException {
        return new ArrayList<>(Arrays.asList(Repo.class));
    }
    
    @Override
    public Pixmap getPixmap(String path) {
        if (pixmapCache.containsKey(path)) return pixmapCache.get(path);
        Pixmap p = super.getPixmap(path);
        if (p == null) return null;
        pixmapCache.put(path, p);
        return p;
    }
}
