package com.qmo.facebook.steps;

import com.qmo.facebook.pages.BasePage;
import org.jbehave.core.annotations.AfterStories;
import org.jbehave.core.annotations.AfterStory;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.BeforeStory;
import org.picocontainer.annotations.Inject;
import org.springframework.stereotype.Component;

//import com.swacorp.qmo.jbehave.util.StoryData;

@Component
public class BeforeOrAfterStories extends BasePage {

    @Inject
   // StoryData storyData;
    @BeforeStory
    public void beforeStory() {
        System.out.print("@ARD BeforeStory::Started Here!!!");
    }

    @AfterStory
    public void afterStory() {
        //driver.close();
        System.out.print("@ARD AfterStory::End Here!!!");
    }

    @BeforeStories
    public void beforeStories() {
        System.out.print("@ARD BeforeStories::Started Here!!!");
    }

    @AfterStories
    public void afterStories() {
        driver.close();
        System.out.print("@ARD AfterStories::End Here!!!");
    }

}
