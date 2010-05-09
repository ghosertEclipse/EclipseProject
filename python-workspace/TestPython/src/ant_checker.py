'''
Created on Jan 21, 2010

@author: jiawzhang
'''

from xml.dom.minidom import parse, Node, Element

# BASE_BUILD_XML_PATH = 'E:\\refactory\\gen31\\'
BASE_BUILD_XML_PATH = 'F:\\Others\\gen31\\'

BUILD_XML_PATH = BASE_BUILD_XML_PATH + 'build.xml'

matchedNum = 0

target_name_list =[]

matchedDependenceNum = 0

# {filename : [targetname1, targetname2]}
changePositionList = {}

new_stubjar_list = ['platform.jar', 'dataModel.jar', 'utilities.jar', 'sharedFacades.jar', 'msgConsumer.jar']

new_stubjar_list_with_websvc = ['platform.jar', 'dataModel.jar', 'utilities.jar', 'sharedFacades.jar', 'commonWebSvc.jar', 'msgConsumer.jar']

def recursiveFindTarget(fileattr, element):
    # element = Element()
    if element.nodeType == Node.ELEMENT_NODE:
        #and element.tagName == 'jar':
        if element.hasAttributes():
            namedNodeMap = element.attributes
            for i in range(namedNodeMap.length):
                attr = namedNodeMap.item(i)
                if attr.nodeValue == '${build.dir}/classes':
                    global matchedNum
                    matchedNum = matchedNum + 1
                    print matchedNum
                    print fileattr
                    print
                    parentElement = element.parentNode
                    while True:
                        if parentElement.nodeName == 'target':
                            print parentElement.toxml()
                            # Handling finding target name
                            global target_name_list
                            target_name_list.append(parentElement.getAttribute('name'))
                            break
                        else:
                            parentElement = parentElement.parentNode
                    print
                    print
    if element.hasChildNodes():
        for childNode in element.childNodes:
            recursiveFindTarget(fileattr, childNode)
        
def recursiveFindDependence(fileattr, element):            
    if element.nodeType == Node.ELEMENT_NODE:
        if element.nodeName == 'target' and element.hasAttribute('depends'):
            depend_jars = element.getAttribute('depends')
            depend_jar_list = depend_jars.split(',')
            for jar in depend_jar_list:
                global target_name_list
                if jar.strip() in target_name_list:
                    global matchedDependenceNum
                    matchedDependenceNum = matchedDependenceNum + 1
                    print matchedDependenceNum
                    print 'filename: ' + fileattr
                    print element.toxml()
                    global changePositionList
                    if not changePositionList.has_key(fileattr):
                        changePositionList[fileattr] = [element.getAttribute('name')]
                    else:
                        changePositionList[fileattr].append(element.getAttribute('name'))
                    print
                    break
    if element.hasChildNodes():
        for childNode in element.childNodes:
            recursiveFindDependence(fileattr, childNode)
    
mainDoc = parse(BUILD_XML_PATH)

document = mainDoc.documentElement

nodeList = document.getElementsByTagName('import')

# Get all the target name whose element contains '${build.dir}/classes'
# Store values in target_name_list
for importNode in nodeList:
    fileattr = importNode.getAttribute('file')
    importDoc = parse(BASE_BUILD_XML_PATH + fileattr)
    recursiveFindTarget(fileattr, importDoc.documentElement)
    
# Get all the target which rely on target_name_list
# Store values in changePositionList
target_name_list = set(target_name_list)
target_name_list.remove('stubmockjar')
target_name_list.remove('listingmgr.war')
for name in target_name_list:
    print name

for importNode in nodeList:
    fileattr = importNode.getAttribute('file')
    importDoc = parse(BASE_BUILD_XML_PATH + fileattr)
    recursiveFindDependence(fileattr, importDoc.documentElement)

import re
# Change the files based on changePositionList
matchedNumber = 0
for importNode in nodeList:
    fileattr = importNode.getAttribute('file')
    if changePositionList.has_key(fileattr):
        with open(BASE_BUILD_XML_PATH + fileattr) as file:
            nameList = changePositionList[fileattr]
            for line in file:
                for name in nameList:
                    if '<target name="{0}"'.format(name) in line:
                        if name in line:
                            matchedNumber = matchedNumber + 1
                            print matchedNumber
                            print 'fileattr: ' + fileattr
                            print 'name: ' + name
                            print 'line: ' + line
                            # change the current line here.
                            print 'change to:'
                            depends_list = re.search(r'depends="(.*?)"', line).groups()[0].split(',')
                            new_depends_list = []
                            isExtended = False
                            for depends in depends_list:
                                if depends.strip() in target_name_list:
                                    if not isExtended:
                                        if 'WebSvc' in line:
                                            new_depends_list.extend(new_stubjar_list_with_websvc)
                                        else:
                                            new_depends_list.extend(new_stubjar_list)
                                        isExtended = True
                                    else:
                                        pass
                                elif depends.strip() in new_stubjar_list_with_websvc:
                                    pass
                                else:
                                    new_depends_list.append(depends.strip())
                            newline = re.sub(r'depends="(.*?)"', 'depends="{0}"'.format(', '.join(new_depends_list)), line)
                            print 'line: ' + newline
                            print '\n'
                        break
                    