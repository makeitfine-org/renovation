{{/*
    Expand the name of the chart [Base name (chart name, overridable)]
*/}}
{{- define "ca.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
    Create a default fully qualified app name [Fullname (release + chart name, overridable)]
*/}}
{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "ca.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name (include "ca.name" .) | trunc 63 | trimSuffix "-" }}
{{- end -}}
{{- end -}}

{{/*
    Labels common to all resources
*/}}
{{- define "ca.labels" -}}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version | replace "+" "_" }}
app.kubernetes.io/name: {{ include "ca.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
app.kubernetes.io/component: {{ .Chart.Name }}
{{/*app.kubernetes.io/namespace: {{ .Release.Namespace }}*/}}
{{- end -}}

{{/*
    Selector labels (to match pods)
*/}}
{{- define "ca.selectorLabels" -}}
app.kubernetes.io/name: {{ include "ca.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
    Special name helpers for components
*/}}

{{- define "ca.secretName" -}}
{{ printf "%s-secret" (include "ca.fullname" .) | trunc 63 | trimSuffix "-" }}
{{- end -}}

{{- define "ca.configmapName" -}}
{{ printf "%s-config" (include "ca.fullname" .) | trunc 63 | trimSuffix "-" }}
{{- end -}}

{{/*
    Component-specific names
*/}}


{{/*
Fully Qualified Domain Name (FQDN) for Vault Service
!todo: not readY*/}}
{{- define "vault.serviceFQDN" -}}
http://{{ include "vault.fullname" . }}.{{ .Release.Namespace }}.svc.cluster.local:{{ .Values.vault.server.service.port }}
{{- end -}}
{{/*{{ include "vault.serviceFQDN" . | quote }}*/}}


{{/* --- */}}



{{- define "caOld.metadata" -}}
metadata:
  name: {{ include "ca.name" . }}{{- if .nameSuffix }}-{{ .nameSuffix }}{{- end }}
  namespace: {{ .Values.namespaceOverride | default .Release.Namespace }}
{{- end }}


{{- define "ca.fullImage" -}}
{{ .Values.image.repository }}:{{ .Values.image.tag }}
{{- end }}


{{/*define template for deployment*/}}
{{- define "ca.deployment" -}}
apiVersion: apps/v1
kind: {{ default "Deployment" .Values.deployment }}
{{ include "caOld.metadata" . | nindent 0 }}
spec:
  replicas: {{ default 1 .Values.replicas }}
  selector:
    matchLabels:
      app: {{ include "ca.name" . }}
  template:
    metadata:
      labels:
        app: {{ include "ca.name" . }}
    spec:
      containers:
        - name: {{ include "ca.name" . | quote }}
          image: {{ include "ca.fullImage" . | quote }}
          imagePullPolicy: {{ .Values.image.pullPolicy | quote }}
          envFrom:
            - configMapRef:
                name: apps-mongodb-configmap
            - configMapRef:
                name: apps-postgresql-configmap
            - configMapRef:
                name: apps-redis-configmap
            - configMapRef:
                name: apps-info-configmap
            - secretRef:
                name: extsecrets-mongodb-secret
          ports:
            - containerPort: {{ .containerPort -}}

          {{ if and .Values.healthCheck .Values.healthCheck.livenessProbe  }}
          livenessProbe:
            {{- if .Values.healthCheck.livenessProbe.http  }}
            httpGet:
              path: {{ .Values.healthCheck.livenessProbe.http.path }}
              port: {{ .Values.service.port }}
            {{- end }}
            {{- if .Values.healthCheck.livenessProbe.exec  }}
            exec:
              command:
                {{- range .Values.healthCheck.livenessProbe.exec.command }}
                - {{ . | quote }}
                {{- end }}
            {{- end }}
            initialDelaySeconds: {{ .Values.healthCheck.livenessProbe.initialDelaySeconds }}
            periodSeconds: {{ .Values.healthCheck.livenessProbe.periodSeconds }}
            timeoutSeconds: {{ .Values.healthCheck.livenessProbe.timeoutSeconds }}
            failureThreshold: {{ .Values.healthCheck.livenessProbe.failureThreshold }}
          {{- end -}}

          {{ if and .Values.healthCheck .Values.healthCheck.readinessProbe }}
          readinessProbe:
            {{- if .Values.healthCheck.readinessProbe.http  }}
            httpGet:
              path: {{ .Values.healthCheck.livenessProbe.http.path }}
              port: {{ .Values.service.port }}
            {{- end }}
            {{- if .Values.healthCheck.readinessProbe.exec  }}
            exec:
              command:
                {{- range .Values.healthCheck.readinessProbe.exec.command }}
                - {{ . | quote }}
                {{- end }}
            {{- end }}
            initialDelaySeconds: {{ .Values.healthCheck.readinessProbe.initialDelaySeconds }}
            periodSeconds: {{ .Values.healthCheck.readinessProbe.periodSeconds }}
            timeoutSeconds: {{ .Values.healthCheck.readinessProbe.timeoutSeconds }}
            failureThreshold: {{ .Values.healthCheck.readinessProbe.failureThreshold }}
          {{- end -}}

          {{ if .Values.resources }}
          resources:
            limits:
                cpu: {{ .Values.resources.limits.cpu }}
                memory: {{ .Values.resources.limits.memory }}
            requests:
                cpu: {{ .Values.resources.requests.cpu }}
                memory: {{ .Values.resources.requests.memory }}
          {{- end }}
{{- end }}


{{/*define template for service*/}}
{{- define "ca.service" -}}
apiVersion: v1
kind: Service
{{ include "caOld.metadata" . | nindent 0 }}
spec:
  selector:
    app: {{ .Values.name }}
  type: {{ .Values.service.type }}
  ports:
    - port: {{ .Values.service.port }}
      targetPort: {{ .targetPort }}
      nodePort: {{ .Values.service.nodePort }}
{{- end }}
