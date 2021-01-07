podTemplate(yaml: """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: some-label-value
spec:
  containers:
  - name: mysql
    image: mysql
    command:
    - cat
    tty: true
"""
) {
    node(POD_LABEL) {
      container('mysql') {
        sh """ 
        mysql -h mysql.jenkins -u root -ppassword -e 'CREATE DATABASE IF NOT EXISTS db_test_projet;'
        """
      }
    }
}