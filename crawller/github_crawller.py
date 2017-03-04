"""
abca dfs.
"""

from bs4 import BeautifulSoup as bs

import requests, time, os, random

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

    url = 'https://github.com/search?q=language%3AJava+stars%3A%3E%3D1&ref=searchresults&type=Repositories&utf8=%E2%9C%93&p='
    crawll_count = 10
    # crawll_count = 383177
    page_count = int(crawll_count / 10) + 1 + (1 if (crawll_count % 10) > 0 else 0)
    sleep_time_max_count = 10

    for i in range(1, page_count):
        url_query = url + str(i)
        repo_list_a_tags = get_repo_list_a_tags(url_query)

        for repo_list_a_tag in repo_list_a_tags:
            repo_url = "/" + repo_list_a_tag.text
            print (repo_url)

            print (os.system('cd project_data; git clone https://github.com' + repo_url + ".git"))
            sleep_time = random.randrange(0, sleep_time_max_count)

            time.sleep(sleep_time)
            # print(os.wait())