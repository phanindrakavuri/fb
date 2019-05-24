package com.qmo;

import com.qmo.facebook.configurations.EDAConfigurations;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.failures.BatchFailures;
import org.jbehave.core.i18n.LocalizedKeywords;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.model.ExamplesTableFactory;
import org.jbehave.core.parsers.RegexPrefixCapturingPatternParser;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.SimpleThreadScope;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.io.CodeLocations.getPathFromURL;
import static org.jbehave.core.reporters.Format.*;

public class EDAJbehaveStories extends JUnitStories {
    Logger logger = Logger.getLogger(EDAJbehaveStories.class);
    private static final String _STORIES_SEPARATOR = ",";
    private CrossReference xref = new CrossReference().withJsonOnly().withOutputAfterEachStory(false);

    private ApplicationContext applicationContext;

    static {
        String path = EDAJbehaveStories.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "log4j.properties";
        PropertyConfigurator.configure(path);
        String keyStoreStr = System.getProperty("user.dir") + "\\src\\main\\resources\\cert\\cacerts";
        System.setProperty("javax.net.ssl.trustStore", keyStoreStr);
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
    }

    Logger log = Logger.getLogger(EDAJbehaveStories.class);

    public EDAJbehaveStories() {
        configuredEmbedder()
                .embedderControls()
                .doGenerateViewAfterStories(true)
                .doIgnoreFailureInStories(true)
                .doIgnoreFailureInView(true)
                .useThreads(30)
                .useStoryTimeoutInSecs(120000);
    }

    private List<String> getMetaFilters() {

        String metaFilterStr = System.getProperty("meta.filter");
        metaFilterStr = metaFilterStr == null ? "" : metaFilterStr;
        String release = System.getProperty("release");
        logger.info("release :: " + logger);
        String env = "";
        if (StringUtils.isNotEmpty(release) && release.equals("R1")) {
            env = ",+PDT";
        } else {
            env = ",+UAT";
        }
        metaFilterStr = metaFilterStr.concat(env);
        List<String> metaFilters = Arrays.asList(metaFilterStr.split(","));
        log.info("**** MetaFilters = " + metaFilters);
        return metaFilters;
    }

/*    @Bean
    public static CustomScopeConfigurer registerCustomScopes(){

        CustomScopeConfigurer csc = new CustomScopeConfigurer();
        csc.addScope("thread", new SimpleThreadScope());
        return csc;
    }*/

    @Bean
    public static BeanFactoryPostProcessor registerCustomScopes(){

        return new BeanFactoryPostProcessor() {
            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
                beanFactory.registerScope("thread", new SimpleThreadScope());
            }
        };
    }
    @Override
    protected List<String> storyPaths() {
        final List<String> globList = new ArrayList<String>();
        final String[] globs = storyFilter().split(_STORIES_SEPARATOR);
        for (final String story : globs) {
            globList.add("**/*" + story + (story.endsWith(".story") ? "" : ".story"));
        }
        log.info("******************************************************************************");
        log.info("**** globList = " + globList);
        log.info("******************************************************************************");
        String classpath = getClassPath();
        log.info("classpath=" + classpath);
        List<String> paths = new StoryFinder().findPaths(classpath, globList, null);
        log.info("**** story paths = " + paths.toString());
        return paths;
    }

    private String storyFilter() {
        String storyFilter = System.getProperty("story.filter");
        if (storyFilter == null) {
            storyFilter = "";
        }
        return storyFilter;
    }

    private String getClassPath() {
        URL fileUrl = codeLocationFromClass(this.getClass());
        String classpath = getPathFromURL(fileUrl);
        log.info("getClassPath classpath = " + classpath);
        // class may come from a jar file
        if (classpath.endsWith(".jar!")) {
            int idx = classpath.lastIndexOf('.');
            classpath = classpath.substring(0, idx);
        }
        return classpath;
    }

    @Override
    public Configuration configuration() {
        Class<? extends Embeddable> embeddableClass = this.getClass();

        Properties viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");
        viewResources.put("reports", "ftl/jbehave-reports-with-totals.ftl");

        // Start from default ParameterConverters instance
        ParameterConverters parameterConverters = new ParameterConverters();

        // factory to allow parameter conversion and loading from external resources (used by StoryParser too)
        ExamplesTableFactory examplesTableFactory = new ExamplesTableFactory(new LocalizedKeywords(),
                new LoadFromClasspath(embeddableClass), parameterConverters);

        // add custom converters
        parameterConverters.addConverters(new ParameterConverters.DateConverter(new SimpleDateFormat("yyyy-MM-dd")),
                new ParameterConverters.ExamplesTableConverter(examplesTableFactory));


        URL location = null;
        if (applicationContext == null) {
            location = CodeLocations.codeLocationFromPath("target/classes");
        } else {
            try {
                location = new URL(CodeLocations.codeLocationFromClass(EDAJbehaveStories.class).toString() + "public/jbehave");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        log.info("---------------------- " + location);

        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(embeddableClass))
                .useStoryParser(new RegexStoryParser(examplesTableFactory))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withCodeLocation(location)
                        .withDefaultFormats()
                        .withViewResources(viewResources)
                        .withFormats(CONSOLE, TXT, HTML_TEMPLATE, XML_TEMPLATE)
                        .withFailureTrace(true)
                        .withFailureTraceCompression(true)
                        .withCrossReference(xref)
                        .withReporters(new TestReporter()))
                .useParameterConverters(parameterConverters)
                // use '%' instead of '$' to identify parameters
                .useStepPatternParser(new RegexPrefixCapturingPatternParser(
                        "$"))
                .useStepMonitor(xref.getStepMonitor());

    }

    @Override
    public InjectableStepsFactory stepsFactory() {
       /* this.applicationContext = ApplicationContextProvider.applicationContext;
        log.info("EDAstories ac: " + this.applicationContext);*/
        if (this.applicationContext == null) {
            //this.applicationContext = new SpringApplicationContextFactory("classpath:config.xml").createApplicationContext();
            AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(EDAConfigurations.class);
            this.applicationContext=applicationContext;
        }
        log.info(this.applicationContext);

        return new SpringStepsFactory(configuration(), this.applicationContext);
    }


    // To run a story: java -Dstory.filter=mystory.story org.junit.runner.JUnitCore JBehaveStories
    @Override
    public void run() throws Throwable {

        Embedder embedder = configuredEmbedder();
        embedder.useMetaFilters(getMetaFilters());
        try {
            int numberOfTimes = 1;
            //story.repeat is an optional parameter, which can be given per maven build
            if (System.getProperty("story.repeat") != null) {
                try {
                    numberOfTimes = Integer.parseInt(System.getProperty("story.repeat"));
                } catch (NumberFormatException nfe) {
                    numberOfTimes = 1;
                }
            }
            BatchFailures batchFailures = null;
            int runIndex = numberOfTimes;
            while (runIndex-- > 0) {
                try {
                    embedder.runStoriesAsPaths(storyPaths());
                } catch (Exception e) {
                    if (numberOfTimes == 1) {
                        throw e;
                    }
                    if (batchFailures == null) {
                        batchFailures = new BatchFailures();
                    }
                    batchFailures.put("Run-" + (numberOfTimes - runIndex), e);
                }
            }
            if (batchFailures != null) {
                throw new Embedder.RunningStoriesFailed(batchFailures);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            embedder.generateCrossReference();
        }
    }
}
