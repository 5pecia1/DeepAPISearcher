"""
abca dfs.
"""

from bs4 import BeautifulSoup as bs

import requests, time

def get_repo_list_a_tags(link):
    """
    return repo a tag in repo list
    """

    req = requests.get(link)
    soup = bs(req.text, 'html.parser')

    repo_list = soup.find('ul', {'class': 'js-repo-list'})
    repo_list_li = repo_list.find_all('h3')

    return [repo.find('a') for repo in repo_list_li]

if __name__ == "__main__":

    url = 'https://github.com/search?l=&q=language%3AJava+stars%3A%3E1&ref=advsearch&type=Repositories&utf8=%E2%9C%93&p='
    crawll_count = 100
    page_count = int(crawll_count / 10)
    sleep_time = 5

    for i in range(page_count):
        i += 1
        time.sleep(sleep_time)
        url_query = url + str(i)
        repo_list_a_tags = get_repo_list_a_tags(url_query)

        for repo_list_a_tag in repo_list_a_tags:
            repo_url = "/" + repo_list_a_tag.text
            print (repo_url)
