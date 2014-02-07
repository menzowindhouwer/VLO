package eu.clarin.cmdi.vlo.pages;

import eu.clarin.cmdi.vlo.VloWicketApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestFacetedSearchPage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new VloWicketApplication());
	}

	@Test
	public void homepageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(FacetedSearchPage.class);

		//assert rendered page class
		tester.assertRenderedPage(FacetedSearchPage.class);
	}
}