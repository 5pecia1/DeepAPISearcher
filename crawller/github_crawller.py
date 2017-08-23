"""
from github
"""
import time
import os
import sys
import random
import requests
from bs4 import BeautifulSoup as bs

def get_repo_list(link):
    """
    return repo list
    """

    req = requests.get(link)
    soup = bs(req.text, 'html.parser')

    return soup.find('ul', {'class': 'js-repo-list'})

def get_repo_urls(repo_ul_tag):
    """
    retrun repo urls
    """

    repo_titles = repo_ul_tag.find_all('h3')

    def get_url(title):
        """
        return github url + repo title
        """
        a_tag = title.find('a')
        return "https://github.com/" + a_tag.text

    return map(get_url, repo_titles)

def get_star_value(stargazers):
    """
    parse stargazers for star value
    """

    stargazers = str(stargazers)

    SVG = "</svg>"
    svg_end = stargazers.index(SVG) + len(SVG)

    stargazers = stargazers[svg_end:]

    A_TAG = "</a>"
    a_start = stargazers.index(A_TAG)

    stargazers = stargazers[:a_start]

    stargazers = stargazers.strip()

    stargazers = stargazers.replace(',', '')

    k = 1
    append = 0
    if "k" in stargazers:
        k = 1000
        append = 999
        stargazers = stargazers.replace('k', '')

    stargazers = float(stargazers)
    stargazers = stargazers * k

    return int(stargazers) + append

def get_start_star(repo_ul_tag):
    """
    return first start value
    """
    stargazers = repo_ul_tag.find_all('a', {'class': 'muted-link'})

    return get_star_value(stargazers[0])

def get_end_star(repo_ul_tag):
    """
    return last start value
    """

    stargazers = repo_ul_tag.find_all('a', {'class': 'muted-link'})

    return get_star_value(stargazers[-1])

if __name__ == "__main__":

    URL = "https://github.com/search?\
    o=desc&p={0}&q=language%3AJava+stars%3A1..{1}&\
    ref=searchresults&s=stars&type=Repositories&utf8=%E2%9C%93"
    SAVE_PATH = '/media/sol/26A745B1362009C1/java_projects'
    CRAWLL_COUNT = 500000
    SLEEP_TIME_MAX_COUNT = 10
    END_PAGE = 100

    count = 0
    #start_star = 30000
    start_star = 37

    while start_star >= 1:
        page = 1
        print("start star : ", start_star)

        while page <= END_PAGE:
            print("page : ", page)
            URL_QUERY = URL.format(page, start_star)
            repo_list = get_repo_list(URL_QUERY)

            error_count = 0
            while repo_list is None:
                error_count += 1
                print("repo list is None\nError count : ", error_count)
                time.sleep(SLEEP_TIME_MAX_COUNT)
                repo_list = get_repo_list(URL_QUERY)

            repo_urls = get_repo_urls(repo_list)

            for repo_url in repo_urls:
                print("repo url : ", repo_url)
                splited = repo_url.split("/")
                fileName = splited[-2] + "_" + splited[-1]
                os.system('cd ' + SAVE_PATH + '; git clone ' + repo_url + ".git " + fileName)

                count += 1
                print("count : ", count)

                if count == CRAWLL_COUNT:
                    print("Success!\nmax count!")
                    print("set count : ", CRAWLL_COUNT)
                    print("current count : ", count)
                    print("===program end===")
                    sys.exit(1)

                sleep_time = random.randrange(1, SLEEP_TIME_MAX_COUNT)
                time.sleep(sleep_time)

            if page == 1:
                start_star = get_start_star(repo_list)
                print("current start star : ", start_star)
            elif page == END_PAGE:
                end_star = get_end_star(repo_list)
                print("end star : ", end_star)

                if start_star == end_star:
                    start_star -= 1
                else:
                    start_star = end_star

                print("next start star : ", start_star)

            page += 1
