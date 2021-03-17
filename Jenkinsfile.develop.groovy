#!groovy
/* groovylint-disable CompileStatic, DuplicateStringLiteral, NestedBlockDepth */

pipeline {
    agent { label 'jenkins-fat' }


    parameters {
        booleanParam(name: 'APPLY_DEV_ANALYTICS', defaultValue: false, description: 'Should we deploy the dev branch state of the develop stage?')
    }

    environment {
        DEVOPS_PLATFORM_CLUSTER = 'https://master.ocp02.cloud.lhind.app.lufthansa.com' // The OpenShift cluster url
        DEVOPS_PLATFORM_CLUSTER_NAMESPACE = "sherlock-dev" // The stage-specific namespace name
        DEVOPS_PLATFORM_CLUSTER_NAMESPACE_TOKEN = 'jnknsb_openshift_p_lhind_sherlock-dev_write' // The stage-specific namespace token, saved as an openShift client credential
        APP_ANALYTICS_NAME = "${env.BRANCH_NAME == 'develop' ? 'sherlock-analytics' : 'sherlock-analytics'}" // + env.BRANCH_NAME}" // The app name used in the OpenShift resources
    }

    /* ***** ***** ***** ***** ***** ***** STAGES ***** ***** ***** ***** ***** ***** */

    stages {

        stage('DEV - Deploy Analytics-Service') {
            when {
                allOf {
                    branch 'develop'
                    expression {
                        env.BRANCH_NAME == 'develop' && params.APPLY_DEV_ANALYTICS
                    }
                }
            }
            
            steps {
                script {
                    openshift.withCluster(DEVOPS_PLATFORM_CLUSTER) {
                        openshift.withCredentials(DEVOPS_PLATFORM_CLUSTER_NAMESPACE_TOKEN) {
                            openshift.withProject(DEVOPS_PLATFORM_CLUSTER_NAMESPACE) {
                                openshift.raw("start-build ${APP_ANALYTICS_NAME} --from-dir=./ --follow")
                            }
                        }
                    }
                }
            }
        }



       

    } // STAGES
} // PIPELINE
