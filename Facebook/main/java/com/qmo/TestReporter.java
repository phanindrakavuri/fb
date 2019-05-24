package com.qmo;

import org.apache.log4j.Logger;
import org.jbehave.core.model.*;
import org.jbehave.core.reporters.StoryReporter;

import java.util.List;
import java.util.Map;

public class TestReporter implements StoryReporter {
    Logger logger = Logger.getLogger(TestReporter.class);

    @Override
    public void storyNotAllowed(Story story, String filter) {

    }

    @Override
    public void storyCancelled(Story story, StoryDuration storyDuration) {

    }

    @Override
    public void beforeStory(Story story, boolean givenStory) {
        //logger.info("********************"+story.getName()+" START*************************");
    }

    @Override
    public void afterStory(boolean givenStory) {

    }

    @Override
    public void narrative(Narrative narrative) {

    }

    @Override
    public void lifecyle(Lifecycle lifecycle) {

    }

    @Override
    public void scenarioNotAllowed(Scenario scenario, String filter) {

    }

    @Override
    public void beforeScenario(String scenarioTitle) {

    }

    @Override
    public void scenarioMeta(Meta meta) {

    }

    @Override
    public void afterScenario() {

    }

    @Override
    public void givenStories(GivenStories givenStories) {

    }

    @Override
    public void givenStories(List<String> storyPaths) {

    }

    @Override
    public void beforeExamples(List<String> steps, ExamplesTable table) {

    }

    @Override
    public void example(Map<String, String> tableRow) {

    }

    @Override
    public void afterExamples() {

    }

    @Override
    public void beforeStep(String step) {

    }

    @Override
    public void successful(String step) {

        //logger.info("********************"+step +" START*************************");
    }

    @Override
    public void ignorable(String step) {

    }

    @Override
    public void pending(String step) {

    }

    @Override
    public void notPerformed(String step) {

    }

    @Override
    public void failed(String step, Throwable cause) {

        logger.error(step+" failed ", cause);
    }

    @Override
    public void failedOutcomes(String step, OutcomesTable table) {

    }

    @Override
    public void restarted(String step, Throwable cause) {

    }

    @Override
    public void dryRun() {

    }

    @Override
    public void pendingMethods(List<String> methods) {

    }
}
