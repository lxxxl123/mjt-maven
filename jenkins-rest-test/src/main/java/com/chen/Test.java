package com.chen;

import cn.hutool.http.HtmlUtil;

import java.util.regex.Pattern;

public class Test {


    public static String formatHtml(String s) {
        return HtmlUtil.cleanHtmlTag(s.replaceAll("\n]", "]"));
    }
    public static void main(String[] args) {
        System.out.println(formatHtml("  <!DOCTYPE html><html class=\"\"><head resURL=\"/static/5288b818\" data-rooturl=\"\" data-resurl=\"/static/5288b818\" data-extensions-available=\"true\" data-unit-test=\"false\" data-imagesurl=\"/static/5288b818/images\" data-crumb-header=\"Jenkins-Crumb\" data-crumb-value=\"8206cb43ab5cb8ec7e20494e568c9f451bbeed65e326d08412ad732839569e0b\">\n" +
                "    \n" +
                "    \n" +
                "\n" +
                "    <title>QMS » qms-platform-build #390 Console [Jenkins\n" +
                "]</title><link rel=\"stylesheet\" href=\"/static/5288b818/jsbundles/base-styles-v2.css\" type=\"text/css\"><link rel=\"stylesheet\" href=\"/static/5288b818/css/color.css\" type=\"text/css\"><link rel=\"stylesheet\" href=\"/static/5288b818/css/responsive-grid.css\" type=\"text/css\"><link rel=\"shortcut icon\" href=\"/static/5288b818/favicon.ico\" type=\"image/vnd.microsoft.icon\"><script src=\"/static/5288b818/scripts/prototype.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/scripts/behavior.js\" type=\"text/javascript\"></script><script src='/adjuncts/5288b818/org/kohsuke/stapler/bind.js' type='text/javascript'></script><script src=\"/static/5288b818/scripts/yui/yahoo/yahoo-min.js\"></script><script src=\"/static/5288b818/scripts/yui/dom/dom-min.js\"></script><script src=\"/static/5288b818/scripts/yui/event/event-min.js\"></script><script src=\"/static/5288b818/scripts/yui/animation/animation-min.js\"></script><script src=\"/static/5288b818/scripts/yui/dragdrop/dragdrop-min.js\"></script><script src=\"/static/5288b818/scripts/yui/container/container-min.js\"></script><script src=\"/static/5288b818/scripts/yui/connection/connection-min.js\"></script><script src=\"/static/5288b818/scripts/yui/datasource/datasource-min.js\"></script><script src=\"/static/5288b818/scripts/yui/autocomplete/autocomplete-min.js\"></script><script src=\"/static/5288b818/scripts/yui/menu/menu-min.js\"></script><script src=\"/static/5288b818/scripts/yui/element/element-min.js\"></script><script src=\"/static/5288b818/scripts/yui/button/button-min.js\"></script><script src=\"/static/5288b818/scripts/yui/storage/storage-min.js\"></script><script src=\"/static/5288b818/scripts/polyfills.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/scripts/hudson-behavior.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/scripts/sortable.js\" type=\"text/javascript\"></script><link rel=\"stylesheet\" href=\"/static/5288b818/scripts/yui/container/assets/container.css\" type=\"text/css\"><link rel=\"stylesheet\" href=\"/static/5288b818/scripts/yui/container/assets/skins/sam/container.css\" type=\"text/css\"><link rel=\"stylesheet\" href=\"/static/5288b818/scripts/yui/menu/assets/skins/sam/menu.css\" type=\"text/css\"><link rel=\"stylesheet\" href=\"/static/5288b818/jsbundles/ui-refresh-overrides.css\" type=\"text/css\"><link rel=\"search\" href=\"/opensearch.xml\" type=\"application/opensearchdescription+xml\" title=\"Jenkins\"><meta name=\"ROBOTS\" content=\"INDEX,NOFOLLOW\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><script src=\"/adjuncts/5288b818/org/kohsuke/stapler/jquery/jquery.full.js\" type=\"text/javascript\"></script><script>var Q=jQuery.noConflict()</script><script>\n" +
                "      if(window.Prototype && JSON) {\n" +
                "        var _json_stringify = JSON.stringify;\n" +
                "        JSON.stringify = function(value) {\n" +
                "            var _array_tojson = Array.prototype.toJSON;\n" +
                "            delete Array.prototype.toJSON;\n" +
                "            var r=_json_stringify(value);\n" +
                "            Array.prototype.toJSON = _array_tojson;\n" +
                "            return r;\n" +
                "    };\n" +
                "}  \n" +
                "   </script><script src=\"/static/5288b818/plugin/extended-choice-parameter/js/selectize.min.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/plugin/extended-choice-parameter/js/jsoneditor.min.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/plugin/extended-choice-parameter/js/jquery.jsonview.min.js\" type=\"text/javascript\"></script><link rel=\"stylesheet\" href=\"/static/5288b818/plugin/extended-choice-parameter/css/jquery.jsonview.css\"><link rel=\"stylesheet\" id=\"icon_stylesheet\" href=\"/static/5288b818/plugin/extended-choice-parameter/css/selectize.css\"><link rel=\"stylesheet\" id=\"icon_stylesheet\" href=\"/static/5288b818/plugin/extended-choice-parameter/css/selectize.bootstrap2.css\"><link rel=\"stylesheet\" id=\"theme_stylesheet\"><link rel=\"stylesheet\" id=\"icon_stylesheet\"><script src=\"/static/5288b818/jsbundles/vendors.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/jsbundles/page-init.js\" type=\"text/javascript\"></script><script src=\"/static/5288b818/jsbundles/sortable-drag-drop.js\" type=\"text/javascript\"></script></head><body data-model-type=\"hudson.model.FreeStyleBuild\" id=\"jenkins\" class=\"yui-skin-sam two-column jenkins-2.342\" data-version=\"2.342\"><a href=\"#skip2content\" class=\"skiplink\">Skip to content</a><header id=\"page-header\" class=\"page-header\"><div class=\"page-header__brand\"><div class=\"logo\"><a id=\"jenkins-home-link\" href=\"/\"><img src=\"/static/5288b818/images/svgs/logo.svg\" alt=\"[Jenkins]\" id=\"jenkins-head-icon\"><img src=\"/static/5288b818/images/title.svg\" alt=\"Jenkins\" width=\"139\" id=\"jenkins-name-icon\" height=\"34\"></a></div><a href=\"/\" class=\"page-header__brand-link\"><img src=\"/static/5288b818/images/svgs/logo.svg\" alt=\"[Jenkins]\" class=\"page-header__brand-image\"><span class=\"page-header__brand-name\">Jenkins</span></a></div><div class=\"searchbox hidden-xs\"><form role=\"search\" method=\"get\" name=\"search\" action=\"/job/QMS/job/qms-platform-build/390/search/\" style=\"position:relative;\" class=\"no-json\"><div id=\"search-box-sizer\"></div><div id=\"searchform\"><input role=\"searchbox\" name=\"q\" placeholder=\"查找\" id=\"search-box\" class=\"main-search__input\"><span class=\"main-search__icon-leading\"><svg class=\"\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><path d=\"M221.09 64a157.09 157.09 0 10157.09 157.09A157.1 157.1 0 00221.09 64z\" fill=\"none\" stroke=\"currentColor\" stroke-miterlimit=\"10\" stroke-width=\"32\"/><path fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-miterlimit=\"10\" stroke-width=\"32\" d=\"M338.29 338.29L448 448\"/></svg></span><a href=\"https://www.jenkins.io/redirect/search-box\" class=\"main-search__icon-trailing\"><svg class=\"\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 512 512\"><path d=\"M256 40a216 216 0 10216 216A216 216 0 00256 40z\" fill=\"none\" stroke=\"currentColor\" stroke-miterlimit=\"10\" stroke-width=\"38\"/><path d=\"M200 202.29s.84-17.5 19.57-32.57C230.68 160.77 244 158.18 256 158c10.93-.14 20.69 1.67 26.53 4.45 10 4.76 29.47 16.38 29.47 41.09 0 26-17 37.81-36.37 50.8S251 281.43 251 296\" fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-miterlimit=\"10\" stroke-width=\"38\"/><circle cx=\"250\" cy=\"360\" r=\"25\" fill=\"currentColor\"/></svg></a><div id=\"search-box-completion\"></div><script>createSearchBox(\"/job/QMS/job/qms-platform-build/390/search/\");</script></div></form></div><div class=\"login page-header__hyperlinks\"><div id=\"visible-am-insertion\" class=\"page-header__am-wrapper\"></div><div id=\"visible-sec-am-insertion\" class=\"page-header__am-wrapper\"></div><a href=\"/user/dev\" class=\"model-link\"><svg viewBox=\"0 0 24 24\" focusable=\"false\" class=\"svg-icon am-monitor-icon\"><use href=\"/static/5288b818/images/material-icons/svg-sprite-social-symbol.svg#ic_person_24px\"></use></svg><span class=\"hidden-xs hidden-sm\">开发人员</span></a><a href=\"/logout\"><svg viewBox=\"0 0 24 24\" focusable=\"false\" class=\"svg-icon \"><use href=\"/static/5288b818/images/material-icons/svg-sprite-action-symbol.svg#ic_input_24px\"></use></svg><span class=\"hidden-xs hidden-sm\">注销</span></a></div></header><div id=\"breadcrumbBar\"><script src='/adjuncts/5288b818/lib/layout/breadcrumbs.js' type='text/javascript'></script><div class=\"top-sticker noedge\"><div class=\"top-sticker-inner\"><div class=\"jenkins-breadcrumbs\"><ul id=\"breadcrumbs\"><li class=\"item\"><a href=\"/\" class=\"model-link\">Dashboard</a></li><li href=\"/\" class=\"children\"></li><li class=\"item\"><a href=\"/job/QMS/\" class=\"model-link\">QMS</a></li><li href=\"/job/QMS/\" class=\"children\"></li><li class=\"item\"><a href=\"/job/QMS/job/qms-platform-build/\" class=\"model-link\">qms-platform-build</a></li><li href=\"/job/QMS/job/qms-platform-build/\" class=\"children\"></li><li class=\"item\"><a href=\"/job/QMS/job/qms-platform-build/390/\" class=\"model-link\">#390</a></li><li class=\"separator\"></li></ul><div id=\"breadcrumb-menu-target\"></div></div></div></div></div><div id=\"page-body\" class=\"clear\"><div id=\"side-panel\"><div id=\"tasks\"><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/\" class=\"task-link \"><span class=\"task-icon-link\"><svg class=\"icon-up icon-md\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><path fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\" d=\"M112 244l144-144 144 144M256 120v292\"/></svg></span><span class=\"task-link-text\">返回到工程</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/\" class=\"task-link \"><span class=\"task-icon-link\"><svg class=\"\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><rect x=\"96\" y=\"48\" width=\"320\" height=\"416\" rx=\"48\" ry=\"48\" fill=\"none\" stroke=\"currentColor\" stroke-linejoin=\"round\" stroke-width=\"32\"/><path fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\" d=\"M176 128h160M176 208h160M176 288h80\"/></svg></span><span class=\"task-link-text\">状态集</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/changes\" class=\"task-link \"><span class=\"task-icon-link\"><svg class=\"\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><path fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\" d=\"M160 368L32 256l128-112M352 368l128-112-128-112M304 96l-96 320\"/></svg></span><span class=\"task-link-text\">变更记录</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/console\" class=\"task-link task-link--active\"><span class=\"task-icon-link\"><svg class=\"icon-terminal icon-xlg\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><rect x=\"32\" y=\"48\" width=\"448\" height=\"416\" rx=\"48\" ry=\"48\" fill=\"none\" stroke=\"currentColor\" stroke-linejoin=\"round\" stroke-width=\"32\"/><path fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\" d=\"M96 112l80 64-80 64M192 240h64\"/></svg></span><span class=\"task-link-text\">控制台输出</span></a></span><div class=\"subtasks\"><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/consoleText\" class=\"task-link \"><span class=\"task-icon-link\"><svg class=\"icon-document icon-md\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><path d=\"M416 221.25V416a48 48 0 01-48 48H144a48 48 0 01-48-48V96a48 48 0 0148-48h98.75a32 32 0 0122.62 9.37l141.26 141.26a32 32 0 019.37 22.62z\" fill=\"none\" stroke=\"currentColor\" stroke-linejoin=\"round\" stroke-width=\"32\"/><path d=\"M256 56v120a32 32 0 0032 32h120M176 288h160M176 368h160\" fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\"/></svg>\n" +
                "</span><span class=\"task-link-text\">文本方式查看</span></a></span></div></div></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/configure\" class=\"task-link \"><span class=\"task-icon-link\"><?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<svg class=\"\" class=\"\" aria-hidden=\"true\" width=\"512px\" height=\"512px\" viewBox=\"0 0 512 512\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n" +
                "    <g stroke=\"none\" stroke-width=\"1\" fill=\"none\" fill-rule=\"evenodd\">\n" +
                "        <g transform=\"translate(50.378500, 42.000000)\" stroke=\"currentColor\" stroke-width=\"32\">\n" +
                "            <path d=\"M211.9115,150.31 C185.005783,147.652748 159.332619,162.189611 147.769706,186.628874 C136.206793,211.068138 141.24875,240.137201 160.366525,259.254975 C179.484299,278.37275 208.553362,283.414707 232.992626,271.851794 C257.431889,260.288881 271.968752,234.615717 269.3115,207.71 C266.263249,177.360931 242.260569,153.358251 211.9115,150.31 L211.9115,150.31 Z M410.9165,277.125 C411.707607,281.564887 410.903766,286.141508 408.647035,290.04607 L372.853966,351.97469 C370.607176,355.862053 367.040281,358.81433 362.8015,360.295 C358.482479,361.803699 353.868429,362.272089 349.334332,361.662103 L320.522752,357.785994 C299.879684,355.00882 279.988607,366.61094 272.243485,385.946453 L261.688643,412.296365 C259.969284,416.588704 257.239668,420.402837 253.7315,423.415 C250.285888,426.373452 245.894629,428 241.353189,428 L169.796876,428 C165.308275,428 160.960823,426.431216 157.5065,423.565 C153.996701,420.652754 151.25415,416.924675 149.518855,412.70702 L138.358864,385.582532 C130.453683,366.368896 110.519057,354.968685 89.9503733,357.89869 L61.8696222,361.898788 C57.3434346,362.543543 52.7286498,362.09476 48.4115003,360.59 C44.1729946,359.112652 40.6059709,356.162306 38.3598585,352.276117 L2.56596537,290.34607 C0.309233973,286.441508 -0.494606827,281.864887 0.296500273,277.425 C1.10103427,272.909758 2.99390287,268.65863 5.81124857,265.039618 L23.4896432,242.330895 C36.4067894,225.738218 36.3997393,202.490855 23.4725314,185.906016 L5.73914207,163.155131 C2.93726987,159.560495 1.06449867,155.330725 0.286500273,150.84 C-0.479118527,146.420732 0.330889273,141.87293 2.57525137,137.989769 L38.3890342,76.0253095 C40.6358245,72.1379474 44.2027191,69.1856702 48.4415003,67.705 C52.7614677,66.1959703 57.3747149,65.7192136 61.9119868,66.3128907 L91.2373143,70.1499486 C111.792442,72.839474 131.546967,61.2195013 139.18365,41.9470371 L149.578712,15.7133384 C151.281675,11.4156234 154.004147,7.5964633 157.5115,4.585 C160.957112,1.6265481 165.348371,0 169.889811,0 L241.446124,0 C245.934725,0 250.282177,1.5687841 253.7365,4.435 C257.245602,7.3466683 259.982278,11.0789954 261.704008,15.3012287 L272.612929,42.053395 C280.490247,61.3711009 300.470109,72.8820729 321.133157,70.007291 L349.370217,66.0787615 C353.899388,65.4486329 358.513486,65.904939 362.8315,67.41 C367.070006,68.8873483 370.637029,71.837694 372.883142,75.7238833 L408.677035,137.65393 C410.933766,141.558492 411.737607,146.135113 410.9465,150.575 C410.140427,155.098876 408.314604,159.379748 405.607548,163.092841 L385.561411,190.588825 C373.755974,206.781577 374.583692,228.954539 387.563545,244.222054 L405.168289,264.929585 C408.130553,268.41394 410.114244,272.622543 410.9165,277.125 Z\"></path>\n" +
                "        </g>\n" +
                "    </g>\n" +
                "</svg></span><span class=\"task-link-text\">编辑编译信息</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/confirmDelete\" class=\"task-link \"><span class=\"task-icon-link\"><svg class=\"icon-edit-delete icon-md\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><path d=\"M112 112l20 320c.95 18.49 14.4 32 32 32h184c17.67 0 30.87-13.51 32-32l20-320\" fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\"/><path stroke=\"currentColor\" stroke-linecap=\"round\" stroke-miterlimit=\"10\" stroke-width=\"32\" d=\"M80 112h352\"/><path d=\"M192 112V72h0a23.93 23.93 0 0124-24h80a23.93 23.93 0 0124 24h0v40M256 176v224M184 176l8 224M328 176l-8 224\" fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\"/></svg></span><span class=\"task-link-text\">删除构建 ‘#390’</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/parameters\" class=\"task-link \"><span class=\"task-icon-link\"><img src=\"/static/5288b818/images/svgs/document-properties.svg\" style=\"width: 24px; height: 24px; \"></span><span class=\"task-link-text\">Parameters</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/injectedEnvVars\" class=\"task-link \"><span class=\"task-icon-link\"><img src=\"/static/5288b818/images/svgs/document-properties.svg\" style=\"width: 24px; height: 24px; \"></span><span class=\"task-link-text\">Environment Variables</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/390/git\" class=\"task-link \"><span class=\"task-icon-link\"><img src=\"/static/5288b818/plugin/git/icons/git-icon.svg\"></span><span class=\"task-link-text\">Git Build Data</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/blue/organizations/jenkins/QMS%2Fqms-platform-build/detail/qms-platform-build/390/\" class=\"task-link \"><span class=\"task-icon-link\"><img src=\"/static/5288b818/plugin/blueocean-rest-impl/images/48x48/blueocean.png\"></span><span class=\"task-link-text\">打开 Blue Ocean</span></a></span></div><div class=\"task \"><span class=\"task-link-wrapper \">\n" +
                "            \n" +
                "            <a href=\"/job/QMS/job/qms-platform-build/384/console\" class=\"task-link \"><span class=\"task-icon-link\"><svg class=\"icon-previous icon-md\" class=\"\" aria-hidden=\"true\" xmlns=\"http://www.w3.org/2000/svg\" class=\"\" viewBox=\"0 0 512 512\"><title></title><path fill=\"none\" stroke=\"currentColor\" stroke-linecap=\"round\" stroke-linejoin=\"round\" stroke-width=\"32\" d=\"M244 400L100 256l144-144M120 256h292\"/></svg></span><span class=\"task-link-text\">上一次构建</span></a></span></div></div></div><div id=\"main-panel\"><a name=\"skip2content\"></a><h1 class=\"build-caption page-headline\"><span style=\"width: 48px; height: 48px; \" class=\"build-status-icon__wrapper icon-red icon-xlg\"><span class=\"build-status-icon__outer\"><svg viewBox=\"0 0 24 24\" tooltip=\"Failed\" focusable=\"false\" class=\"svg-icon \"><use href=\"/images/build-status/build-status-sprite.svg#build-status-static\"></use></svg></span><svg viewBox=\"0 0 24 24\" tooltip=\"Failed\" focusable=\"false\" class=\"svg-icon icon-red icon-xlg\"><use href=\"/static/5288b818/images/build-status/build-status-sprite.svg#last-failed\"></use></svg></span><span class=\"jenkins-icon-adjacent\">控制台输出</span><script>\n" +
                "          (function(){\n" +
                "            function updateBuildCaptionIcon(){\n" +
                "              new Ajax.Request(\"statusIcon\",\n" +
                "        {\n" +
                "                method: \"get\",\n" +
                "                onComplete: function(rsp,_) {\n" +
                "                  var isBuilding = rsp.getResponseHeader(\"X-Building\");\n" +
                "                  if (isBuilding == \"true\") {\n" +
                "                    setTimeout(updateBuildCaptionIcon,\n" +
                "                    5000)\n" +
                "                } else {\n" +
                "                    var progressBar = document.querySelector(\".build-caption-progress-container\");\n" +
                "                    if (progressBar) {\n" +
                "                      progressBar.style.display = \"none\";\n" +
                "                    }\n" +
                "                }\n" +
                "                  document.querySelector(\".build-caption .icon-xlg\").outerHTML = rsp.responseText;\n" +
                "            }\n" +
                "        });\n" +
                "    }\n" +
                "\n" +
                "            window.addEventListener(\"load\", function(){\n" +
                "              Event.observe(window,\n" +
                "        \"jenkins:consoleFinished\", updateBuildCaptionIcon);\n" +
                "    });\n" +
                "})();\n" +
                "        </script></h1><script src='/static/5288b818/extensionList/hudson.console.ConsoleAnnotatorFactory/hudson.plugins.timestamper.annotator.TimestampAnnotatorFactory3/script.js'></script><link rel='stylesheet' type='text/css' href='/static/5288b818/descriptor/hudson.tasks._ant.AntOutcomeNote/style.css' /><script src='/static/5288b818/descriptor/hudson.tasks._ant.AntTargetNote/script.js'></script><script src='/static/5288b818/descriptor/hudson.console.ExpandableDetailsNote/script.js'></script><link rel='stylesheet' type='text/css' href='/static/5288b818/descriptor/hudson.console.ExpandableDetailsNote/style.css' /><link rel='stylesheet' type='text/css' href='/static/5288b818/descriptor/hudson.plugins.gradle.GradleOutcomeNote/style.css' /><script src='/static/5288b818/descriptor/hudson.plugins.gradle.GradleTaskNote/script.js'></script><link rel='stylesheet' type='text/css' href='/static/5288b818/descriptor/hudson.plugins.gradle.GradleTaskNote/style.css' /><script src='/static/5288b818/descriptor/org.jenkinsci.plugins.workflow.job.console.NewNodeConsoleNote/script.js'></script><link rel='stylesheet' type='text/css' href='/static/5288b818/descriptor/org.jenkinsci.plugins.workflow.job.console.NewNodeConsoleNote/style.css' /><pre class=\"console-output\"><span class=\"timestamp\"><b>16: 19: 02</b> </span>Started by user <a href='/user/dev' class='model-link model-link--float'>开发人员</a>\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>Running as SYSTEM\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>[EnvInject\n" +
                "] - Loading node environment variables.\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>Building on the built-in node in workspace /data/var/lib/jenkins/workspace/QMS/qms-platform-build\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>The recommended git tool is: NONE\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>using credential 20788f93-4634-420f-8cd9-5e83cb7bf83f\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span> &gt; git rev-parse --resolve-git-dir /data/var/lib/jenkins/workspace/QMS/qms-platform-build/.git # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>Fetching changes from the remote Git repository\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span> &gt; git config remote.origin.url ssh: //git@git.haday.cn:9022/qms/qms-platform.git # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>Fetching upstream changes from ssh: //git@git.haday.cn:9022/qms/qms-platform.git\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span> &gt; git --version # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span> &gt; git --version # 'git version 1.8.3.1'\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span>using GIT_SSH to set credentials root-gitlab-code\n" +
                "<span class=\"timestamp\"><b>16: 19: 02</b> </span> &gt; git fetch --tags --progress ssh: //git@git.haday.cn:9022/qms/qms-platform.git +refs/heads/*:refs/remotes/origin/* # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 12</b> </span> &gt; git rev-parse refs/remotes/origin/feature/market-complainV1.0.0-front-end^{commit\n" +
                "} # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 12</b> </span>Checking out Revision a5067e8dee23dc41a366813ae712deecadcb6929 (refs/remotes/origin/feature/market-complainV1.0.0-front-end)\n" +
                "<span class=\"timestamp\"><b>16: 19: 12</b> </span> &gt; git config core.sparsecheckout # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 12</b> </span> &gt; git checkout -f a5067e8dee23dc41a366813ae712deecadcb6929 # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 13</b> </span>Commit message: \"temp\"\n" +
                "<span class=\"timestamp\"><b>16: 19: 13</b> </span> &gt; git rev-list --no-walk a5067e8dee23dc41a366813ae712deecadcb6929 # timeout=10\n" +
                "<span class=\"timestamp\"><b>16: 19: 13</b> </span>[qms-platform-build\n" +
                "] $ /data/usr/repo/apache-maven-3.5.2/bin/mvn -s /usr/repo/apache-maven-3.5.2/conf/settings.xml -gs /usr/repo/apache-maven-3.5.2/conf/settings.xml -U clean package -DskipTest -P test\n" +
                "<span class=\"timestamp\"><b>16: 19: 14</b> </span>[INFO\n" +
                "] Scanning for projects...\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Reactor Build Order:\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] \n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] qms-platform\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] qms-service\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] \n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Building qms-platform 1.0.0\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] \n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] <b class=maven-mojo>--- maven-clean-plugin: 2.5:clean (default-clean) @ qms-platform ---\n" +
                "</b><span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] \n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Building qms-service 1.0.0\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=warning-inline>[WARNING\n" +
                "] The POM for com.chen:mybatis-generate:jar: 1.0-SNAPSHOT is missing, no dependency information available\n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Reactor Summary:\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] \n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] qms-platform ....................................... SUCCESS [\n" +
                "    0.149 s\n" +
                "]\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] qms-service ........................................ FAILURE [\n" +
                "    0.007 s\n" +
                "]\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] BUILD FAILURE\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Total time: 0.579 s\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Finished at: 2022-08-01T16: 19: 15+08: 00\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] Final Memory: 8M/147M\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>[INFO\n" +
                "] ------------------------------------------------------------------------\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] Plugin com.chen:mybatis-generate: 1.0-SNAPSHOT or one of its dependencies could not be resolved: Could not find artifact com.chen:mybatis-generate:jar: 1.0-SNAPSHOT -&gt; [Help 1\n" +
                "]\n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] \n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] To see the full stack trace of the errors, re-run Maven with the -e switch.\n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] Re-run Maven using the -X switch to enable full debug logging.\n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] \n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] For more information about the errors and possible solutions, please read the following articles:\n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span><span class=error-inline>[ERROR\n" +
                "] [Help 1\n" +
                "] <a href='http: //cwiki.apache.org/confluence/display/MAVEN/PluginResolutionException'>http://cwiki.apache.org/confluence/display/MAVEN/PluginResolutionException</a>\n" +
                "</span><span class=\"timestamp\"><b>16: 19: 15</b> </span>Build step 'Invoke top-level Maven targets' marked build as failure\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>SSH: Current build result is [FAILURE\n" +
                "], not going to run.\n" +
                "<span class=\"timestamp\"><b>16: 19: 15</b> </span>Finished: FAILURE\n" +
                "</pre></div></div><footer class=\"page-footer\"><div class=\"container-fluid\"><div class=\"page-footer__flex-row\"><div class=\"page-footer__footer-id-placeholder\" id=\"footer\"></div><div class=\"page-footer__links rest_api hidden-xs\"><a href=\"api/\">REST API</a></div><div class=\"page-footer__links page-footer__links--white jenkins_ver\"><a rel=\"noopener noreferrer\" href=\"https://www.jenkins.io/\" target=\"_blank\">Jenkins 2.342</a></div></div></div></footer><script async=\"true\" src=\"/static/5288b818/scripts/svgxuse.min.js\" type=\"text/javascript\"></script></body></html>"));

    }
}
