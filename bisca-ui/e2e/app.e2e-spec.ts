import { BiscaUiPage } from './app.po';

describe('bisca-ui App', function() {
  let page: BiscaUiPage;

  beforeEach(() => {
    page = new BiscaUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
