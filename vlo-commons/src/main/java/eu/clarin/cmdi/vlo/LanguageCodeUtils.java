/*
 * Copyright (C) 2014 CLARIN
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
package eu.clarin.cmdi.vlo;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import eu.clarin.cmdi.vlo.config.VloConfig;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some helper methods for working with language codes, extracted from
 * {@link LanguageCodePostProcessor}
 *
 * @author Thomas Eckart
 */
public class LanguageCodeUtils {

    private final static Logger LOG = LoggerFactory.getLogger(LanguageCodeUtils.class);
    private final static Pattern LANGUAGE_CODE_PATTERN = Pattern.compile(eu.clarin.cmdi.vlo.FacetConstants.LANGUAGE_CODE_PATTERN);

    private Map<String, String> twoLetterCodesMap;
    private Map<String, String> threeLetterCodesMap;
    private Map<String, String> silToIso639Map;
    private Map<String, String> languageNameToIso639Map;
    private Map<String, String> iso639ToLanguageNameMap;
    private Map<String, String> iso639_2TToISO639_3Map;

    private final VloConfig config;

    public LanguageCodeUtils(VloConfig config) {
        this.config = config;
    }

    /**
     *
     * @param langCode an upper case ISO639-3 language code
     * @return the name of the language if it is present in the language code
     * map
     */
    public String getLanguageNameForLanguageCode(String langCode) {
        String result = getIso639ToLanguageNameMap().get(langCode);

        if (result == null) {
            result = langCode;
        }

        return result;
    }

    public Map<String, String> getSilToIso639Map() {
        if (silToIso639Map == null) {
            silToIso639Map = createSilToIsoCodeMap();
        }
        return silToIso639Map;
    }

    public Map<String, String> getTwoLetterCountryCodeMap() {
        if (twoLetterCodesMap == null) {
            twoLetterCodesMap = createCodeMap(config.getLanguage2LetterCodeComponentUrl());
        }
        return twoLetterCodesMap;
    }

    public Map<String, String> getThreeLetterCountryCodeMap() {
        if (threeLetterCodesMap == null) {
            threeLetterCodesMap = createCodeMap(config.getLanguage3LetterCodeComponentUrl());
        }
        return threeLetterCodesMap;
    }

    public Map<String, String> getLanguageNameToIso639Map() {
        if (languageNameToIso639Map == null) {
            languageNameToIso639Map = createReverseCodeMap(config.getLanguage3LetterCodeComponentUrl());
        }
        return languageNameToIso639Map;
    }

    public Map<String, String> getIso639ToLanguageNameMap() {
        if (iso639ToLanguageNameMap == null) {
            iso639ToLanguageNameMap = createCodeMap(config.getLanguage3LetterCodeComponentUrl());
        }

        return iso639ToLanguageNameMap;
    }

    /**
     * Returns map of ISO 639-2/B codes to ISO 639-3
     *
     * It is strongly advised to use ISO 639-3 codes, the support for ISO 639-2
     * may be discontinued in the future
     *
     * @return map of ISO 639-2/B codes to ISO 639-3
     */
    public Map<String, String> getIso6392TToISO6393Map() {
        if (iso639_2TToISO639_3Map == null) {
            iso639_2TToISO639_3Map = new ConcurrentHashMap<>();
            iso639_2TToISO639_3Map.put("alb", "sqi");
            iso639_2TToISO639_3Map.put("arm", "hye");
            iso639_2TToISO639_3Map.put("baq", "eus");
            iso639_2TToISO639_3Map.put("bur", "mya");
            iso639_2TToISO639_3Map.put("cze", "ces");
            iso639_2TToISO639_3Map.put("chi", "zho");
            iso639_2TToISO639_3Map.put("dut", "nld");
            iso639_2TToISO639_3Map.put("fre", "fra");
            iso639_2TToISO639_3Map.put("geo", "kat");
            iso639_2TToISO639_3Map.put("ger", "deu");
            iso639_2TToISO639_3Map.put("gre", "ell");
            iso639_2TToISO639_3Map.put("ice", "isl");
            iso639_2TToISO639_3Map.put("max", "mkd");
            iso639_2TToISO639_3Map.put("mao", "mri");
            iso639_2TToISO639_3Map.put("may", "msa");
            iso639_2TToISO639_3Map.put("per", "fas");
            iso639_2TToISO639_3Map.put("rum", "ron");
            iso639_2TToISO639_3Map.put("slo", "slk");
            iso639_2TToISO639_3Map.put("tib", "bod");
            iso639_2TToISO639_3Map.put("wel", "cym");
        }

        return iso639_2TToISO639_3Map;
    }

    private Map<String, String> createCodeMap(String url) {
        LOG.info("Creating language code map from {}", url);
        try {
            Map<String, String> result = new ConcurrentHashMap<>(CommonUtils.createCMDIComponentItemMap(url));
            return result;
        } catch (Exception e) {
            if (CommonUtils.shouldSwallowLookupErrors()) {
                LOG.warn("Ignoring exception", e);
                return new HashMap<>();
            } else {
                throw new RuntimeException("Cannot instantiate postProcessor. URL: " + url, e);
            }
        }
    }

    private Map<String, String> createReverseCodeMap(String url) {
        LOG.debug("Creating language code map.");
        try {
            Map<String, String> result = new ConcurrentHashMap<>(CommonUtils.createReverseCMDIComponentItemMap(url));
            return result;
        } catch (Exception e) {
            if (CommonUtils.shouldSwallowLookupErrors()) {
                LOG.warn("Ignoring exception", e);
                return new HashMap<>();
            } else {
                throw new RuntimeException("Cannot instantiate postProcessor. URL: " + url, e);
            }
        }
    }

    private Map<String, String> createSilToIsoCodeMap() {
        LOG.debug("Creating silToIso code map.");
        Map<String, String> result = new ConcurrentHashMap<>();
        final String urlString = config.getSilToISO639CodesUrl();

        try {
            URL url = new URL(urlString);
            VTDGen vg = new VTDGen();

            vg.setDoc(IOUtils.toByteArray(url.openStream()));
            vg.parse(true);
            final VTDNav nav = vg.getNav();
            nav.toElement(VTDNav.ROOT);
            AutoPilot ap = new AutoPilot(nav);
            ap.selectXPath("//lang");

            AutoPilot silAp = new AutoPilot(nav);
            silAp.selectXPath("sil");

            AutoPilot isoAp = new AutoPilot(nav);
            isoAp.selectXPath("iso");

            String silContent;
            String isoContent;
            while (ap.evalXPath() != -1) {
                silContent = silAp.evalXPathToString();
                isoContent = isoAp.evalXPathToString();
                result.put(silContent, isoContent);
            }
        } catch(Exception e) {
            if (CommonUtils.shouldSwallowLookupErrors()) {
                LOG.warn("Ignoring exception", e);
                result = new HashMap<>();
            } else {
                throw new RuntimeException("Cannot instantiate postProcessor. URL: " + urlString, e);
            }
        }
        return result;
    }

    public LanguageInfo decodeLanguageCodeString(String fieldValue) {
        if (fieldValue != null) {
            final Matcher matcher = LANGUAGE_CODE_PATTERN.matcher(fieldValue);
            if (matcher.matches() && matcher.groupCount() == 2) {
                final String type = matcher.group(1);
                final String value = matcher.group(2);
                switch (type) {
                    case "code":
                        // value is a language code, look up
                        return new LanguageInfo(LanguageInfo.Type.CODE, value.toUpperCase());
                    case "name":
                        // value is the name to be shown
                        return new LanguageInfo(LanguageInfo.Type.NAME, value);
                }
            }
        }
        return null;
    }

    public static class LanguageInfo {

        public static enum Type {
            CODE, NAME
        }

        private final Type type;
        private final String value;

        private LanguageInfo(Type type, String value) {
            this.type = type;
            this.value = value;
        }

        public Type getType() {
            return type;
        }

        public String getValue() {
            return value;
        }

    }
    
    protected LanguageCodeUtils() {
        //for proxying
        this(null);
    }

}
