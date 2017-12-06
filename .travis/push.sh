#!/bin/sh

setup_git() {
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "Travis CI"
}

commit_javadoc_files() {
  git add docs/javadoc/
  git commit -m "Travis CI auto-generated javadoc on build $TRAVIS_BUILD_NUMBER" -m "<https://travis-ci.org/edinaftc/Ninevolt/$TRAVIS_BUILD_ID>"
}

upload_files() {
  git remote add origin-pages https://${GITHUB_TOKEN}@github.com/edinaftc/Ninevolt.git > /dev/null 2>&1
  git push --quiet --set-upstream origin-pages $TRAVIS_BRANCH
}

setup_git
commit_javadoc_files
upload_files